package com.example.appdev.ui.transactions

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R
import com.example.appdev.database.entities.TransactionsEntity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment : Fragment() {

    private val viewModel: TransactionsViewModel by viewModels()

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val filePath = copyUriToFile(it)
                filePath?.let { path ->
                    viewModel.readCsvFile(path)
                }
            }
        }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)

        val transactionContainer: LinearLayout = view.findViewById(R.id.transactionContainer)
        val addButton: Button = view.findViewById(R.id.addButton)
        val importButton: Button = view.findViewById(R.id.importCSV)
        val totalEarningsTextView: TextView = view.findViewById(R.id.totalEarningsTextView)
        val totalSpentTextView: TextView = view.findViewById(R.id.totalSpentTextView)
        val totalSavedTextView: TextView = view.findViewById(R.id.totalSavedTextView)
        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            transactionContainer.removeAllViews()
            transactions.forEach { transaction ->
                val cardView = createTransactionCard(transaction)
                transactionContainer.addView(cardView)
            }

            totalEarningsTextView.text = "Total Earnings: ${viewModel.calculateTotalEarnings()}$"
            totalSpentTextView.text = "Total Spent: ${viewModel.calculateTotalSpent()}$"
            totalSavedTextView.text = "Total Saved: ${viewModel.calculateTotalSaved()}$"
        })

        addButton.setOnClickListener {
            showAddTransactionDialog()
        }

        importButton.setOnClickListener {
            importTransactionsFromCSV()
        }

        return view
    }

    private fun importTransactionsFromCSV() {
        openFilePicker()
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
                val description = descriptionEditText.text.toString()
                val dateText = dateEditText.text.toString()
                val typeText = typeSpinner.selectedItem.toString()

                // Check if any field is empty
                if (amountText.isEmpty() || description.isEmpty() || dateText.isEmpty() || typeText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val amount = amountText.toFloat()
                val type = typeText[0]

                try {
                    val date = SimpleDateFormat("dd/MM/yy", Locale.US).parse(dateText)
                    val finalAmount = if (type == '-') -amount else amount
                    val transaction = TransactionsEntity(
                        user_id = 1, // Example user_id
                        type = type,
                        amount = finalAmount,
                        currency = "USD", // Example currency
                        date = date,
                        isRecurring = false, // Example value
                        description = description
                    )
                    viewModel.addTransaction(transaction)
                    dialog.dismiss()
                } catch (e: ParseException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Invalid date format", Toast.LENGTH_SHORT).show()
                }
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

    private fun openFilePicker() {
        filePickerLauncher.launch("*/*")
    }

    private fun copyUriToFile(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val fileName = getFileName(uri)
        val file = File(requireContext().cacheDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return file.absolutePath
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "temp_file"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    private fun copyStream(input: InputStream, output: FileOutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }
    }

    private fun createTransactionCard(transaction: TransactionsEntity): CardView {
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
            text = SimpleDateFormat("dd/MM/yy", Locale.US).format(transaction.date)
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
