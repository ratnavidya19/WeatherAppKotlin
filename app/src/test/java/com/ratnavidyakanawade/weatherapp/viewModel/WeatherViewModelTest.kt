package com.ratnavidyakanawade.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ratnavidyakanawade.weatherapp.api.Current
import com.ratnavidyakanawade.weatherapp.api.Daily
import com.ratnavidyakanawade.weatherapp.api.Hourly
import com.ratnavidyakanawade.weatherapp.api.Weather
import com.ratnavidyakanawade.weatherapp.repository.WeatherRepository
import com.ratnavidyakanawade.weatherapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: WeatherRepository
    val weather = Weather(null, emptyList(),emptyList(),"","", "",0)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_loadHourlyWeatherData() = runTest{
        val latitude = "40.77"
        val longitude =  "-73.97"
        Mockito.`when`(repository.getWeatherInfo(latitude, longitude)).thenReturn(Resource.Success(weather))

        val sut = WeatherViewModel(repository)
        sut.loadHourlyWeatherData()
        sut.loadHourlyWeatherData()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.hourlyWeatherList
        Assert.assertEquals(0, result.value!!.size)

    }


    @Test
    fun test_loadHourlyWeatherData_expectingError() = runTest{
        val latitude = "40.77"
        val longitude =  "-73.97"
        Mockito.`when`(repository.getWeatherInfo(latitude, longitude)).thenReturn(Resource.Error("Something went wrong"))

        val sut = WeatherViewModel(repository)
        sut.loadHourlyWeatherData()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.hourlyWeatherList
        Assert.assertEquals(true, result is Resource.Error<*>)
        Assert.assertEquals("Something went wrong", result.value)

    }
    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }



}

