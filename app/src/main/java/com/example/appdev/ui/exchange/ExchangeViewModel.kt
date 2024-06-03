package com.example.appdev.ui.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdev.models.ExchangeRates
import com.example.appdev.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeViewModel : ViewModel() {
    private val _exchangeRates = MutableLiveData<ExchangeRates?>()
    val exchangeRates: LiveData<ExchangeRates?> = _exchangeRates

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchExchangeRates() {
        val call = RetrofitClient.apiService.getLatestRates()

        call.enqueue(object : Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                if (response.isSuccessful) {
                    _exchangeRates.value = response.body()
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