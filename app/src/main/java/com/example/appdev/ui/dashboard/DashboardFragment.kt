package com.example.appdev.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdev.MainActivity
import com.example.appdev.R
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.CardEntity
import com.example.appdev.database.entities.TransactionsEntity
import com.example.appdev.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DashboardFragment : Fragment() {
    private val MIN_SWIPE_DISTANCE = 200
    private var _binding: FragmentDashboardBinding? = null
    private var cardDetails = mutableListOf<CardEntity>()
    private var transactionsList = listOf<List<TransactionsEntity>>()
    private lateinit var expiryDateInput : TextView

    companion object {
        lateinit var selected_card: CardEntity
    }

    private lateinit var db: GoalSaverDatabase
    private lateinit var gestureDetector: GestureDetector
    private var cardIndex = 0

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        db = GoalSaverDatabase.getDatabase(requireContext())
        loadCardsAndTransactions()

        val creditCardView = inflater.inflate(R.layout.item_debit_card, binding.cardContainer, false)

        val valueContainer: TextView = binding.cardValue
        binding.cardContainer.addView(creditCardView)
            creditCardView.setOnTouchListener(object : View.OnTouchListener {
                private var initialX = 0f

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = event.rawX
                        }
                        MotionEvent.ACTION_UP -> {
                            if (cardDetails.size >=1)
                            {
                                val finalX = event.rawX
                                if (initialX - finalX > MIN_SWIPE_DISTANCE) {
                                    // Swipe left detected
                                    cardIndex = (cardIndex + 1) % cardDetails.size
                                    updateCardDetails(creditCardView, cardDetails[cardIndex])
                                    valueContainer.text = "You have ${cardDetails[cardIndex].amount_on_card} $ on this card"
                                    updateTransactions(binding.transactionsRecyclerView, transactionsList[cardIndex])
                                }
                            }

                        }
                    }
                    v.performClick()
                    return true
                }
            })


        binding.addCardButton.setOnClickListener {
            showAddCardDialog()
        }

        return root
    }

    private fun loadCardsAndTransactions() {
        MainActivity.logged_user?.let { user ->
            cardDetails = db.cardDao().getCardsOfUser(user.user_id).toMutableList()
            transactionsList = cardDetails.map { card -> db.transactionDao().getTransactionsByCard(card.card_id) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateCardDetails(view: View, cardDetails: CardEntity) {
        selected_card = cardDetails
        view.findViewById<TextView>(R.id.card_number).text = cardDetails.first_digits_of_card
        view.findViewById<TextView>(R.id.card_holder).text = cardDetails.name_on_card
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val formated_date = dateFormat.format(cardDetails.expiry_date.time).toString()
        view.findViewById<TextView>(R.id.expiry_date).text = formated_date
        view.findViewById<TextView>(R.id.card_value).text = cardDetails.amount_on_card.toString()
    }

    private fun updateTransactions(recyclerView: RecyclerView, transactions: List<TransactionsEntity>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TransactionAdapter(transactions.map {
            Transaction(it.date.toString(), it.description, it.amount.toString())
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun showAddCardDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_card, null)
        val cardNumberInput = dialogLayout.findViewById<EditText>(R.id.card_number_input)
        val cardHolderInput = dialogLayout.findViewById<EditText>(R.id.card_holder_input)
        expiryDateInput = dialogLayout.findViewById<EditText>(R.id.expiry_date_input)
        val currencyTypeInput = dialogLayout.findViewById<EditText>(R.id.currency_type_input)
        val bankNameInput = dialogLayout.findViewById<EditText>(R.id.bank_name_input)
        val addCardButton = dialogLayout.findViewById<Button>(R.id.add_card_button)
        expiryDateInput.setOnClickListener {
            showDatePickerDialog()
        }
        val alertDialog = builder.setView(dialogLayout).create()

        addCardButton.setOnClickListener {
            val cardNumber = cardNumberInput.text.toString()
            val cardHolder = cardHolderInput.text.toString()
            val expiryDate = expiryDateInput.text.toString()
            val currencyType = currencyTypeInput.text.toString()
            val bankName = bankNameInput.text.toString()
            if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && expiryDate.isNotEmpty()) {
                val newCard = CardEntity(
                    first_digits_of_card = cardNumber,
                    user_id = MainActivity.logged_user!!.user_id,
                    expiry_date = SimpleDateFormat("MM/yy").parse(expiryDate),
                    name_on_card = cardHolder,
                    currency_type = currencyType,
                    bank_name = bankName,
                    amount_on_card = 0f
                )
                db.cardDao().insert(newCard)
                cardDetails.add(newCard)
                transactionsList = transactionsList.toMutableList().apply { add(emptyList()) }
                cardIndex = cardDetails.size - 1
                updateCardDetails(binding.cardContainer.getChildAt(0), newCard)
                updateTransactions(binding.transactionsRecyclerView, emptyList())
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            expiryDateInput.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }
    data class Transaction(val date: String, val description: String, val amount: String)
}
