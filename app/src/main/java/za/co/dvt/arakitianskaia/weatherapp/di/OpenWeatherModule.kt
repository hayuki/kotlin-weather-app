package za.co.dvt.arakitianskaia.weatherapp.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import za.co.dvt.arakitianskaia.weatherapp.api.OpenWeatherAPI
import za.co.dvt.arakitianskaia.weatherapp.api.OpenWeatherInterceptor
import javax.inject.Singleton

@Module(includes = arrayOf(GSONModule::class))
class OpenWeatherModule {

    @Provides
    @Singleton
    fun provideApi(gson: Gson): OpenWeatherAPI {

        val apiClient = OkHttpClient.Builder().addInterceptor(OpenWeatherInterceptor()).build()

        return Retrofit.Builder().apply {
            baseUrl(OpenWeatherAPI.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(apiClient)
        }.build().create(OpenWeatherAPI::class.java)
    }
}