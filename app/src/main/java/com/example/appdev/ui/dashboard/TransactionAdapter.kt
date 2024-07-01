package com.example.appdev.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appdev.R

class TransactionAdapter(private val transactions: List<DashboardFragment.Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.transaction_date)
        val descriptionTextView: TextView = itemView.findViewById(R.id.transaction_description)
        val amountTextView: TextView = itemView.findViewById(R.id.transaction_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transactions, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactions[position]
        holder.dateTextView.text = currentTransaction.date
        holder.descriptionTextView.text = currentTransaction.description
        holder.amountTextView.text = currentTransaction.amount

        // Set the text color based on the amount
        val amountValue = currentTransaction.amount.replace("[$,]".toRegex(), "").toFloat()
        if (amountValue < 0) {
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.negativeAmount))
        } else {
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.positiveAmount))
        }
    }

    override fun getItemCount() = transactions.size
}
