package com.example.appdev.ui.transactions

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Gravity
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
import com.example.appdev.MainActivity
import com.example.appdev.R
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.TransactionsEntity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment : Fragment() {
    private val logged_user = MainActivity.logged_user
    private val viewModel: TransactionsViewModel by viewModels()
    private var selectedCardId: Int? = null

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val filePath = copyUriToFile(it)
                filePath?.let { path ->
                    selectedCardId?.let { cardId ->
                        viewModel.readCsvFile(path, cardId)
                    }
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

            totalEarningsTextView.text = "Total Earnings: %.2f$".format(viewModel.calculateTotalEarnings())
            totalSpentTextView.text = "Total Spent: %.2f$".format(viewModel.calculateTotalSpent())
            totalSavedTextView.text = "Total Saved: %.2f$".format(viewModel.calculateTotalSaved())
        })

        addButton.setOnClickListener {
            showAddTransactionDialog()
        }

        importButton.setOnClickListener {
            showCardSelectionDialog()
        }

        return view
    }

    private fun showCardSelectionDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_card, null)
        val cardSpinner = dialogView.findViewById<Spinner>(R.id.spinner_card)

        val cards = logged_user?.let {
            GoalSaverDatabase.getDatabase(this.requireContext()).cardDao()
                .getCardsOfUser(it.user_id)
        }
        var cardNames = cards?.map { it.name_on_card }
        if (cardNames != null) {
            cardNames = cardNames.toMutableList()
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cardNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cardSpinner.adapter = adapter
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Select Card")
            .setView(dialogView)
            .setPositiveButton("Select") { dialog, _ ->
                val selectedCardPosition = cardSpinner.selectedItemPosition
                val selectedCard = cards?.get(selectedCardPosition)
                if (selectedCard != null) {
                    selectedCardId = selectedCard.card_id
                    openFilePicker()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openFilePicker() {
        filePickerLauncher.launch("*/*")
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction, null)
        val cardSpinner = dialogView.findViewById<Spinner>(R.id.spinner_card)
        val typeRadioGroup = dialogView.findViewById<RadioGroup>(R.id.typeRadioGroup)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)
        val dateEditText = dialogView.findViewById<EditText>(R.id.dateEditText)

        dateEditText.setOnClickListener {
            showDatePickerDialog(dateEditText)
        }
        val cards = logged_user?.let {
            GoalSaverDatabase.getDatabase(this.requireContext()).cardDao()
                .getCardsOfUser(it.user_id)
        }
        var cardNames = cards?.map { it.name_on_card }
        if (cardNames != null) {
            cardNames = cardNames.toMutableList()
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cardNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cardSpinner.adapter = adapter
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add Transaction")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val selectedCardPosition = cardSpinner.selectedItemPosition
                val selectedCard = cards?.get(selectedCardPosition)

                val amountText = amountEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val dateText = dateEditText.text.toString()
                val selectedTypeId = typeRadioGroup.checkedRadioButtonId
                val typeText = dialogView.findViewById<RadioButton>(selectedTypeId).text.toString()
                if (amountText.isEmpty() || description.isEmpty() || dateText.isEmpty() || typeText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }

                val amount = amountText.toFloat()
                val type = typeText[0]

                try {
                    val date = SimpleDateFormat("dd/MM/yy", Locale.US).parse(dateText)
                    val finalAmount = if (type == '-') -amount else amount
                    if (logged_user != null) {
                        val transaction = selectedCard?.let {
                            TransactionsEntity(
                                card_id = it.card_id,
                                type = type,
                                amount = finalAmount,
                                date = date,
                                isRecurring = false,
                                description = description
                            )
                        }
                        val card = transaction?.let {
                            GoalSaverDatabase.getDatabase(this.requireContext()).cardDao()
                                .getCardOfId(
                                    it.card_id
                                )
                        }
                        if (transaction != null && card != null) {
                            if (transaction.type == '+') {
                                GoalSaverDatabase.getDatabase(this.requireContext()).cardDao()
                                    .updateCardAmount(
                                        card.card_id,
                                        card.amount_on_card + transaction.amount
                                    )
                            } else {
                                GoalSaverDatabase.getDatabase(this.requireContext()).cardDao()
                                    .updateCardAmount(
                                        card.card_id,
                                        card.amount_on_card + transaction.amount
                                    )
                            }
                            viewModel.addTransaction(transaction)
                        }
                    }

                    dialog.dismiss()
                } catch (e: ParseException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Invalid date format", Toast.LENGTH_SHORT)
                        .show()
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
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
                setMargins(0, 0, 0, 10)
            }
            radius = 16f
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            cardElevation = 0f
        }

        val contentLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val dateTextView = TextView(requireContext()).apply {
            text = SimpleDateFormat("dd/MM/yy", Locale.US).format(transaction.date)
            textSize = 12f
            setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor))
            setTypeface(typeface, Typeface.BOLD)
        }

        val descriptionLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 8)
        }

        val descriptionTextView = TextView(requireContext()).apply {
            text = transaction.description
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.textColor))
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        val deleteButton = ImageButton(requireContext()).apply {
            setImageResource(R.drawable.ic_delete_outline_24)
            setBackgroundResource(android.R.color.white)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 0)
            }
            setPadding(16, 16, 16, 16)
            setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteTransaction(transaction.transaction_id)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        descriptionLayout.addView(descriptionTextView)

        val amountTextView = TextView(requireContext()).apply {
            text = String.format("%.2f$", transaction.amount)
            textSize = 16f
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (transaction.amount < 0) R.color.negative else R.color.positive
                )
            )
            setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 8, 0, 0)
            gravity = Gravity.END
        }

        val separator = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
            ).apply {
                setMargins(0, 8, 0, 0)
            }
            setBackgroundColor(ContextCompat.getColor(context, R.color.secondaryTextColor))
        }

        contentLayout.addView(dateTextView)
        contentLayout.addView(descriptionLayout)
        contentLayout.addView(amountTextView)
        contentLayout.addView(deleteButton)
        contentLayout.addView(separator)
        cardView.addView(contentLayout)

        return cardView
    }

}