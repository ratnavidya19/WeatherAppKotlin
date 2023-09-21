package com.ratnavidyakanawade.weatherapp.repository

import com.ratnavidyakanawade.weatherapp.api.Weather
import com.ratnavidyakanawade.weatherapp.api.WeatherApi
import com.ratnavidyakanawade.weatherapp.utils.Constants.API_KEY
import com.ratnavidyakanawade.weatherapp.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getWeatherInfo(
        lat: String,
        lon: String
    ): Resource<Weather> {
        val response = try {
            api.getWeather(lat, lon, API_KEY)
        } catch (e: Exception) {
            return Resource.Error("Unknown Error occurred.")
        }
        return Resource.Success(response)
    }
}