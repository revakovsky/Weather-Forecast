package com.example.weatherforecast.network

import com.example.weatherforecast.model.models.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("forecast.json")
    fun getForecast(
        @Query("key") key: String,
        @Query("q") city: String,
        @Query("days") days: Int = 10,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no",
    ): Call<Response>

    companion object {
        fun createClient(): ApiClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiClient::class.java)
        }

        private const val URL = "https://api.weatherapi.com/v1/"
    }
}