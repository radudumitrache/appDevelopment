package com.example.appdev.ui.exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appdev.R
import java.text.DecimalFormat

class ExchangeRatesAdapter(private val rates: Map<String, Double>) :
    RecyclerView.Adapter<ExchangeRatesAdapter.RatesViewHolder>() {

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyTextView: TextView = itemView.findViewById(R.id.currencyTextView)
        val rateTextView: TextView = itemView.findViewById(R.id.rateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange_rate, parent, false)
        return RatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val currency = rates.keys.toList()[position]
        val rate = rates[currency]
        val decimalFormat = DecimalFormat("#.##")
        holder.currencyTextView.text = currency
        holder.rateTextView.text = decimalFormat.format(rate)
    }

    override fun getItemCount(): Int = rates.size
}
