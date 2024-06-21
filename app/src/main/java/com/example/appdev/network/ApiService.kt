package com.example.appdev.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest")
    fun getLatestRates(@Query("base") base: String): Call<ExchangeRates>

    @GET("search")
    fun searchATMs(
        @Query("q") query: String,
        @Query("format") format: String,
        @Query("limit") limit: Int,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radius: Int
    ): Call<List<ATMResponse>>
}

data class ExchangeRates(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

data class ATMResponse(
    val lat: Double,
    val lon: Double,
    val display_name: String
)
