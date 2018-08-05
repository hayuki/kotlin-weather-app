package za.co.dvt.arakitianskaia.weatherapp.api

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import za.co.dvt.arakitianskaia.weatherapp.BuildConfig

class OpenWeatherInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url().newBuilder().addQueryParameter("APPID", BuildConfig.OPEN_WEATHER_API_KEY).addQueryParameter("mode", "json").addQueryParameter("units", "metric").build()

        Log.d("FOOO", "current url" + url.toString())

        return chain.proceed(chain.request().newBuilder().addHeader("Accept", "application/json").url(url).build())
    }

}