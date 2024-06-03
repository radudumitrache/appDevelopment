package com.example.appdev.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R

class ExchangeFragment : Fragment() {
    private lateinit var textView: TextView
    private val viewModel: ExchangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange, container, false)
        textView = view.findViewById(R.id.textView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.exchangeRates.observe(viewLifecycleOwner, Observer { exchangeRates ->
            exchangeRates?.let {
                val rates = it.rates.map { (currency, rate) ->
                    "$currency: $rate"
                }.joinToString("\n")

                textView.text = "Base: ${it.base}\nDate: ${it.date}\n\nRates:\n$rates"
            } ?: run {
                textView.text = "No data available"
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.fetchExchangeRates()
    }
}