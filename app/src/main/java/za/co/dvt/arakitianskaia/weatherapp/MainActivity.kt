package za.co.dvt.arakitianskaia.weatherapp

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import za.co.dvt.arakitianskaia.weatherapp.data.ErrorTypes
import za.co.dvt.arakitianskaia.weatherapp.data.ViewModel
import za.co.dvt.arakitianskaia.weatherapp.di.DaggerOpenWeatherComponent
import za.co.dvt.arakitianskaia.weatherapp.di.OpenWeatherModule
import za.co.dvt.arakitianskaia.weatherapp.ui.ForecastAdapter
import za.co.dvt.arakitianskaia.weatherapp.ui.MainPresenter
import za.co.dvt.arakitianskaia.weatherapp.ui.MainView

class MainActivity : AppCompatActivity(), MainView {

    private val MY_PERMISSIONS_FINE_LOCATION: Int = 11
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val presenter = MainPresenter(this)

    private fun injectDI() {
        DaggerOpenWeatherComponent
                .builder()
                .openWeatherModule(OpenWeatherModule())
                .build()
                .inject(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI()
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        forecast.layoutManager = LinearLayoutManager(this)
        forecast.adapter = ForecastAdapter(this)

    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null)
                            getForecast(location.latitude, location.longitude)
                    }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.lastLocation
                                .addOnSuccessListener { location: Location? ->
                                    if (location != null)
                                        getForecast(location.latitude, location.longitude)
                                }
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun getForecast(lat: Double, long: Double) = presenter.getWeather(lat, long)

    override fun showSpinner() {
        showProgress(true)
    }

    override fun hideSpinner() {
        showProgress(false)//To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorToast(errorType: ErrorTypes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateWeather(weather: ViewModel) {
        currentMain.text = getString(R.string.degrees, weather.current.temperature.currentTemp)
        currentMainLabel.text = getWeatherString(weather.current.weather)
        current.text = getString(R.string.degrees, weather.current.temperature.currentTemp)
        min.text = getString(R.string.degrees, weather.current.temperature.minTemp)
        max.text = getString(R.string.degrees, weather.current.temperature.maxTemp)

        imgBackground.setImageResource(getWeatherBackground(weather.current.weather))
        background.setImageResource(getWeatherColour(weather.current.weather))


        (forecast.adapter as ForecastAdapter).addForecast(weather.forecast)
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        layout_main.visibility = if (show) View.GONE else View.VISIBLE
        layout_main.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        layout_main?.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        layout_progress.visibility = if (show) View.VISIBLE else View.GONE
        layout_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        layout_progress?.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    private fun getWeatherBackground(code: Int): Int {
        return when (code) {
            in 200..531 -> R.drawable.forest_rainy
            in 701..781 -> R.drawable.forest_cloudy
            800 -> R.drawable.forest_sunny
            in 801..804 -> R.drawable.forest_cloudy
            else -> R.drawable.forest_cloudy
        }
    }

    private fun getWeatherColour(code: Int): Int {
        return when (code) {
            in 200..531 -> R.color.colorRainy
            in 701..781 -> R.color.colorCloudy
            800 -> R.color.colorSunny
            in 801..804 -> R.color.colorCloudy
            else -> R.color.colorCloudy
        }
    }

    private fun getWeatherString(code: Int): String {
        return when (code) {
            in 200..531 -> getString(R.string.rainy)
            in 701..781 -> getString(R.string.cloudy)
            800 -> getString(R.string.sunny)
            in 801..804 -> getString(R.string.cloudy)
            else -> getString(R.string.cloudy)
        }
    }
}
