package za.co.dvt.arakitianskaia.weatherapp.ui

import za.co.dvt.arakitianskaia.weatherapp.data.CurrentViewModel
import za.co.dvt.arakitianskaia.weatherapp.data.ErrorTypes
import za.co.dvt.arakitianskaia.weatherapp.data.ForecastItemViewModel
import za.co.dvt.arakitianskaia.weatherapp.data.ViewModel

interface MainView {
    fun showSpinner()
    fun hideSpinner()
    fun updateWeather(weather: ViewModel)
    fun showErrorToast(errorType: ErrorTypes)
}