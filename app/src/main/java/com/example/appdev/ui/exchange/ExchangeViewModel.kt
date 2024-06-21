package com.example.appdev.ui.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdev.network.ExchangeRates
import com.example.appdev.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeViewModel : ViewModel() {
    private val _exchangeRates = MutableLiveData<ExchangeRates?>()
    val exchangeRates: LiveData<ExchangeRates?> = _exchangeRates

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchExchangeRates(base: String, amount: Double) {
        val call: Call<ExchangeRates> = RetrofitClient.apiServiceExchange.getLatestRates(base)

        call.enqueue(object : Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                if (response.isSuccessful) {
                    response.body()?.let { exchangeRates ->
                        val adjustedRates = exchangeRates.rates.mapValues { (_, rate) ->
                            rate * amount
                        }
                        _exchangeRates.value = exchangeRates.copy(rates = adjustedRates)
                    }
                } else {
                    _error.value = "Failed to get response: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
