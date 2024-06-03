package com.example.appdev.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R

class TransactionsFragment : Fragment() {

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)
        val transactionContainer: LinearLayout = view.findViewById(R.id.transactionContainer)
        val addButton: Button = view.findViewById(R.id.addButton)

        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            transactionContainer.removeAllViews()
            transactions.forEach { transaction ->
                val cardView = createTransactionCard(transaction)
                transactionContainer.addView(cardView)
            }
        })

        addButton.setOnClickListener {
            // Example of adding a new transaction
            viewModel.addTransaction(TransactionsViewModel.Transaction(-20, "New Item", "12/11/24"))
        }

        return view
    }

    private fun createTransactionCard(transaction: TransactionsViewModel.Transaction): CardView {
        val cardView = CardView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            radius = 16f
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
            cardElevation = 8f
        }

        val contentLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val amountTextView = TextView(requireContext()).apply {
            text = "${transaction.amount}$"
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, if (transaction.amount < 0) R.color.negative else R.color.positive))
        }

        val descriptionTextView = TextView(requireContext()).apply {
            text = transaction.description
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, R.color.description))
        }

        val dateTextView = TextView(requireContext()).apply {
            text = transaction.date
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.date))
        }

        contentLayout.addView(amountTextView)
        contentLayout.addView(descriptionTextView)
        contentLayout.addView(dateTextView)
        cardView.addView(contentLayout)

        return cardView
    }
}
