package com.example.appdev.network

import com.example.appdev.models.ExchangeRates
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("latest?to=USD,GBP")
    fun getLatestRates(): Call<ExchangeRates>
}