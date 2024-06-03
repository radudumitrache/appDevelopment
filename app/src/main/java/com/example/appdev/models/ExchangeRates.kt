package com.example.appdev.models

data class ExchangeRates(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)