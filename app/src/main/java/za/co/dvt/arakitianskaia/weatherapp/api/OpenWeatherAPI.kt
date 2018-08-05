package za.co.dvt.arakitianskaia.weatherapp.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import za.co.dvt.arakitianskaia.weatherapp.data.CurrentWeatherResponse
import za.co.dvt.arakitianskaia.weatherapp.data.ForecastWeatherResponse

interface OpenWeatherAPI {
    @GET("weather")
    fun getCurrentWeatherByLocation(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Observable<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecastByLocation(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Observable<ForecastWeatherResponse>

    companion object {
        val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }
}