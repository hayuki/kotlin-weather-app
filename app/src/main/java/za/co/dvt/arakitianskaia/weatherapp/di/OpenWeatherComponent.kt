package za.co.dvt.arakitianskaia.weatherapp.di

import dagger.Component
import za.co.dvt.arakitianskaia.weatherapp.ui.MainPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(OpenWeatherModule::class))
interface OpenWeatherComponent {
    fun inject(presenter: MainPresenter);
}