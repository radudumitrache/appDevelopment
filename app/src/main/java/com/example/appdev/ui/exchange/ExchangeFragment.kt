package com.example.appdev.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdev.R

class ExchangeFragment : Fragment() {
    private lateinit var amountEditText: EditText
    private lateinit var baseTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var ratesRecyclerView: RecyclerView
    private lateinit var baseCurrencySpinner: Spinner
    private lateinit var fetchButton: Button
    private val viewModel: ExchangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange, container, false)
        amountEditText = view.findViewById(R.id.amountEditText)
        baseTextView = view.findViewById(R.id.baseTextView)
        dateTextView = view.findViewById(R.id.dateTextView)
        ratesRecyclerView = view.findViewById(R.id.ratesRecyclerView)
        baseCurrencySpinner = view.findViewById(R.id.baseCurrencySpinner)
        fetchButton = view.findViewById(R.id.fetchButton)

        ratesRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currencies = listOf("USD", "EUR", "GBP", "JPY", "AUD")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        baseCurrencySpinner.adapter = adapter

        fetchButton.setOnClickListener {
            val selectedCurrency = baseCurrencySpinner.selectedItem.toString()
            val amountText = amountEditText.text.toString()
            val amount = amountText.toDoubleOrNull()

            if (amount != null) {
                viewModel.fetchExchangeRates(selectedCurrency, amount)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.exchangeRates.observe(viewLifecycleOwner, Observer { exchangeRates ->
            exchangeRates?.let {
                val rates = it.rates.map { (currency, rate) ->
                    "$currency: ${rate * (amountEditText.text.toString().toDoubleOrNull() ?: 1.0)}"
                }.joinToString("\n")

                baseTextView.visibility = View.VISIBLE
                dateTextView.visibility = View.VISIBLE
                baseTextView.text = "Base: ${it.base}"
                dateTextView.text = "Date: ${it.date}"
                ratesRecyclerView.adapter = ExchangeRatesAdapter(it.rates)
            } ?: run {
                Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
