package com.ratnavidyakanawade.weatherapp.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratnavidyakanawade.weatherapp.models.DailyWeatherList
import com.ratnavidyakanawade.weatherapp.repository.WeatherRepository
import com.ratnavidyakanawade.weatherapp.utils.Constants
import com.ratnavidyakanawade.weatherapp.utils.Resource
import com.ratnavidyakanawade.weatherapp.utils.getDayFromDate
import com.ratnavidyakanawade.weatherapp.utils.getTemperatureInCelsiusInteger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    var dailyWeatherList = mutableStateOf<List<DailyWeatherList>>(listOf())
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var tomorrowWeatherType = mutableStateOf("")
    var tomorrowTempHigh = mutableStateOf("")
    var tomorrowTempLow = mutableStateOf("")
    var tomorrowImgUrl = mutableStateOf("")

    init {
        loadDailyWeatherData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDailyWeatherData() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getWeatherInfo(Constants.LATITUDE, Constants.LONGITUDE)) {
                is Resource.Success -> {
                    val dailyEntry = result.data!!.daily.mapIndexed { _, entry ->
                        val day = getDayFromDate(entry.dt)
                        val imgUrl = entry.weather[0].icon
                        val weatherType = entry.weather[0].main
                        val highTemp = getTemperatureInCelsiusInteger(entry.temp.max)
                        val lowTemp = getTemperatureInCelsiusInteger(entry.temp.min)
                        DailyWeatherList(day, imgUrl, weatherType, highTemp, lowTemp)
                    }
                    dailyWeatherList.value += dailyEntry
                    tomorrowWeatherType.value = dailyWeatherList.value[0].weatherType
                    tomorrowTempHigh.value = dailyWeatherList.value[0].maxTemp
                    tomorrowTempLow.value = dailyWeatherList.value[0].minTemp
                    tomorrowImgUrl.value = dailyWeatherList.value[0].img
                    dailyWeatherList.value = dailyWeatherList.value.subList(1, 8)
                    loadError.value = ""
                    isLoading.value = false
                }
                is Resource.Error -> {
                    Timber.e("error")
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

}