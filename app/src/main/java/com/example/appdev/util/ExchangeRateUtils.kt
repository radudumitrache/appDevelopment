package com.example.appdev.util

object ExchangeRateUtils {
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "GBP" to 0.75,
    )

    fun getExchangeRate(fromCurrency: String, toCurrency: String): Double {
        val fromRate = exchangeRates[fromCurrency] ?: error("Unknown currency: $fromCurrency")
        val toRate = exchangeRates[toCurrency] ?: error("Unknown currency: $toCurrency")
        return toRate / fromRate
    }
}
