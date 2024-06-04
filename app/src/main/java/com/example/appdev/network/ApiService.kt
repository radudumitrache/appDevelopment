package com.example.appdev.network

import com.example.appdev.models.ExchangeRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest")
    fun getLatestRates(@Query("base") base: String): Call<ExchangeRates>
}
