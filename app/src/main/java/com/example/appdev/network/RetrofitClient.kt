package com.example.appdev.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_EXCHANGE = "https://api.frankfurter.app/"
    private const val BASE_URL_NOMINATIM = "https://nominatim.openstreetmap.org/"

    private val retrofitExchange: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_EXCHANGE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitNominatim: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_NOMINATIM)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServiceExchange: ApiService = retrofitExchange.create(ApiService::class.java)
    val apiServiceNominatim: ApiService = retrofitNominatim.create(ApiService::class.java)
}
