package za.co.dvt.arakitianskaia.weatherapp.ui

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import za.co.dvt.arakitianskaia.weatherapp.api.OpenWeatherAPI
import za.co.dvt.arakitianskaia.weatherapp.data.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainPresenter(val view: MainView) {
    @Inject
    lateinit var api: OpenWeatherAPI

    fun getWeather(lat: Double, long: Double) {
        this.getWeatherFromAPI(lat, long).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<ViewModel> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                view.hideSpinner()
            }

            override fun onNext(data: ViewModel) {
                view.updateWeather(data)
            }

            override fun onComplete() {
                view.hideSpinner()
            }
        })
    }

    private fun getWeatherFromAPI(lat: Double, long: Double): Observable<ViewModel> {

        view.showSpinner()

        val current = api.getCurrentWeatherByLocation(lat, long)
        val forecast = api.getForecastByLocation(lat, long)

        return Observable.zip(current, forecast, BiFunction<CurrentWeatherResponse, ForecastWeatherResponse, ViewModel> { currentVal, forecastVal ->

            val currentModel = CurrentViewModel(currentVal.description.first().id, currentVal.temperature)
            val forecastModel: MutableList<ForecastItemViewModel> = mutableListOf()

            var cal: Calendar = Calendar.getInstance()
            val format = SimpleDateFormat("EEEE", Locale.getDefault())
            Log.d("DATE", format.format(Date(forecastVal.forecast[0].date * 1000L)))
            forecastVal.forecast.forEach { detail ->

                val temp: Calendar = Calendar.getInstance()
                temp.time = Date(detail.date * 1000L)

                if (cal.get(Calendar.DAY_OF_MONTH) != temp.get(Calendar.DAY_OF_MONTH) && temp.get(Calendar.AM_PM) == Calendar.PM) {

                    cal = temp
                    val item: ForecastItemViewModel = ForecastItemViewModel(format.format(cal.time), detail.description.first().icon, detail.description.first().id, detail.temperature.maxTemp)

                    forecastModel.add(item);
                }

            }

            val response = ViewModel(currentModel, forecastModel)
            response

        })

    }
}