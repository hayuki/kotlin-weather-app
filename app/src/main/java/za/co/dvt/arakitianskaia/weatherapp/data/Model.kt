package za.co.dvt.arakitianskaia.weatherapp.data

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(@SerializedName("city") val city: City,
                                   @SerializedName("list") val forecast: List<ForecastDetail>)

data class CurrentWeatherResponse(@SerializedName("weather") val description: List<WeatherDescription>,
                                  @SerializedName("main") val temperature: Temperature,
                                  @SerializedName("dt") val date: Long)

data class City(@SerializedName("name") val cityName: String,
                @SerializedName("country") val country: String)

data class ForecastDetail(@SerializedName("dt") val date: Long,
                          @SerializedName("main") val temperature: Temperature,
                          @SerializedName("weather") val description: List<WeatherDescription>,
                          @SerializedName("dt_txt") val dateTxt: String)

data class Temperature(@SerializedName("temp") val currentTemp: Double,
                       @SerializedName("temp_min") val minTemp: Double,
                       @SerializedName("temp_max") val maxTemp: Double)

data class WeatherDescription(@SerializedName("id") val id: Int,
                              @SerializedName("main") val main: String,
                              @SerializedName("description") val description: String,
                              @SerializedName("icon") val icon: String)

data class ForecastItemViewModel(val degreeDay: String,
                                 val icon: String = "01d",
                                 val weather: Int,
                                 val maxTemp: Double)

data class ViewModel(val current: CurrentViewModel, val forecast: List<ForecastItemViewModel>)

data class CurrentViewModel(val weather: Int, val temperature: Temperature)