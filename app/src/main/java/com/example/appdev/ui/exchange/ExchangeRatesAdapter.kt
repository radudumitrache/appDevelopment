package com.example.appdev.ui.exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExchangeRatesAdapter(private val rates: Map<String, Double>) :
    RecyclerView.Adapter<ExchangeRatesAdapter.RatesViewHolder>() {

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyTextView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return RatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val currency = rates.keys.toList()[position]
        val rate = rates[currency]
        holder.currencyTextView.text = "$currency: $rate"
    }

    override fun getItemCount(): Int = rates.size
}
