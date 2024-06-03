package com.example.appdev.ui.transactions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R
import java.text.SimpleDateFormat
import java.util.*

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
            showAddTransactionDialog()
        }

        return view
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction, null)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.typeSpinner)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)
        val dateEditText = dialogView.findViewById<EditText>(R.id.dateEditText)

        dateEditText.setOnClickListener {
            showDatePickerDialog(dateEditText)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add Transaction")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val amountText = amountEditText.text.toString()
                val amount = if (amountText.isEmpty()) 0 else amountText.toInt()
                val description = descriptionEditText.text.toString()
                val date = dateEditText.text.toString()
                val type = typeSpinner.selectedItem.toString()
                val finalAmount = if (type == "-") -amount else amount
                viewModel.addTransaction(TransactionsViewModel.Transaction(finalAmount, description, date))
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showDatePickerDialog(dateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
            dateEditText.setText(sdf.format(calendar.time))
        }

        DatePickerDialog(
            requireContext(), dateSetListener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
