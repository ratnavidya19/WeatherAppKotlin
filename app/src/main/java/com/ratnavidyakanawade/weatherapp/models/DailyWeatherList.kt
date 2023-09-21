package com.ratnavidyakanawade.weatherapp.models

data class DailyWeatherList(
    val day: String,
    val img: String,
    val weatherType: String,
    val maxTemp: String,
    val minTemp: String
)
