package com.example.appdev.util

object ExchangeRateUtils {
    private val exchangeRates = mapOf(
        "USD" to 1.0, // Base currency
        "EUR" to 0.85,
        "GBP" to 0.75,
        // Add more currencies as needed
    )

    fun getExchangeRate(fromCurrency: String, toCurrency: String): Double {
        val fromRate = exchangeRates[fromCurrency] ?: error("Unknown currency: $fromCurrency")
        val toRate = exchangeRates[toCurrency] ?: error("Unknown currency: $toCurrency")
        return toRate / fromRate
    }
}
