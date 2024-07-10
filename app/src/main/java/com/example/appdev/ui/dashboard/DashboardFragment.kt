package com.example.appdev.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdev.LoginActivity
import com.example.appdev.MainActivity
import com.example.appdev.R
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.CardEntity
import com.example.appdev.database.entities.TransactionsEntity
import com.example.appdev.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat

class DashboardFragment : Fragment() {
    private val MIN_SWIPE_DISTANCE = -200
    private var _binding: FragmentDashboardBinding? = null
    private var cardDetails = mutableListOf<CardEntity>()
    private var transactionsList = listOf<List<TransactionsEntity>>()
    companion object
    {
        lateinit var selected_card : CardEntity
    }
    private lateinit var db: GoalSaverDatabase
    private lateinit var gestureDetector: GestureDetector
    private var cardIndex = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        val root: View = binding.root
        db = GoalSaverDatabase.getDatabase(requireContext())
        loadCardsAndTransactions()

        val creditCardView = inflater.inflate(R.layout.item_debit_card,binding.cardContainer,false)

        val value_container:TextView = binding.cardValue
        binding.cardContainer.addView(creditCardView)
        creditCardView.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = event.rawX
                    }

                    MotionEvent.ACTION_UP -> {
                        val finalX = event.rawX
                        if (initialX - finalX > MIN_SWIPE_DISTANCE) {
                            // Swipe left detected
                            cardIndex = (cardIndex + 1) % cardDetails.size
                            updateCardDetails(creditCardView, cardDetails[cardIndex])
                            value_container.text ="You have ${cardDetails[cardIndex].amount_on_card} $ on this card"
                            updateTransactions(binding.transactionsRecyclerView, transactionsList[cardIndex])
//                            val recyclerView = binding.transactionsRecyclerView
//                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
//                            recyclerView.adapter = TransactionAdapter(transactionsList[cardIndex])
                        }
                    }
                }
                v.performClick()
                return true
            }
        })
        binding.addCardButton.setOnClickListener {

        }
        return root
    }
    private fun loadCardsAndTransactions()
    {
        if (MainActivity.logged_user != null)
        {
            cardDetails = db.cardDao().getCardsOfUser(MainActivity.logged_user!!.user_id).toMutableList()
        }
        transactionsList = cardDetails.map { card ->  db.transactionDao().getTransactionsByCard(card.card_id) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateCardDetails(view: View, PcardDetails: CardEntity) {
        selected_card = PcardDetails
        view.findViewById<TextView>(R.id.card_number).text = PcardDetails.first_digits_of_card
        view.findViewById<TextView>(R.id.card_holder).text = PcardDetails.name_on_card.toString()
        view.findViewById<TextView>(R.id.expiry_date).text = PcardDetails.expiry_date.toString()
        view.findViewById<TextView>(R.id.card_value).text =  PcardDetails.amount_on_card.toString()
    }
    private fun updateTransactions(recyclerView: RecyclerView, transactions: List<TransactionsEntity>) {
        recyclerView.adapter = TransactionAdapter(transactions.map { Transaction(it.date.toString(), it.description, it.amount.toString()) })
    }
    @SuppressLint("MissingInflatedId")
    private fun showAddCardDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_card, null)
        val cardNumberInput = dialogLayout.findViewById<EditText>(R.id.card_number_input)
        val cardHolderInput = dialogLayout.findViewById<EditText>(R.id.card_holder_input)
        val currencyTypeInput = dialogLayout.findViewById<EditText>(R.id.currency_type_input)
        val bankNameInput = dialogLayout.findViewById<EditText>(R.id.bank_name_input)
        val expiryDateInput = dialogLayout.findViewById<EditText>(R.id.bank_name_input)

        with(builder) {
            setTitle("Add New Card")
            setView(dialogLayout)
            setPositiveButton("Add") { dialog, which ->
                val cardNumber = cardNumberInput.text.toString()
                val cardHolder = cardHolderInput.text.toString()
                val expiryDate = expiryDateInput.text.toString()
                val currency_type = currencyTypeInput.text.toString()
                val bank_name = bankNameInput.text.toString()
                if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && expiryDate.isNotEmpty()) {
                    val newCard = CardEntity(
                        first_digits_of_card = cardNumber,
                        user_id = MainActivity.logged_user!!.user_id, // Assuming you have user ID available
                        expiry_date = SimpleDateFormat("MM/yy").parse(expiryDate),
                        name_on_card = cardHolder,
                        currency_type = "USD",
                        bank_name = "Bank", // Example value
                        amount_on_card = 0f
                    )
                    db.cardDao().insert(newCard)
                    cardDetails.add(newCard)
                    transactionsList = transactionsList.toMutableList().apply { add(emptyList()) }
                    cardIndex = cardDetails.size - 1
                    updateCardDetails(binding.cardContainer.getChildAt(0), newCard)
                    updateTransactions(binding.transactionsRecyclerView, emptyList())
                }
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }
    data class CardDetails(val cardNumber: String, val cardHolder: String, val expiryDate: String , val Sum : Float)
    data class Transaction(val date: String, val description: String, val amount: String)
}