package com.example.appdev.ui.dashboard

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
import com.example.appdev.R
import com.example.appdev.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val MIN_SWIPE_DISTANCE = -200
    private var _binding: FragmentDashboardBinding? = null
    private val cardDetails = mutableListOf(
        CardDetails("1234 5678 9012 3456", "John Doe", "12/24", 12.3f),
        CardDetails("9876 5432 1098 7654", "Jane Smith", "11/23", 50.0f),
        CardDetails("4567 8901 2345 6789", "Alice Johnson", "10/22", 49.0f)
    )
    private val transactionsList = listOf(
        listOf(
            Transaction("2024-06-01", "Grocery Store", "-$50.00"),
            Transaction("2024-06-02", "Online Shopping", "-$120.00"),
            Transaction("2024-06-03", "Restaurant", "-$45.00"),
            Transaction("2024-06-03", "Gym Membership", "-$10.00"),
            Transaction("2024-05-23", "Movie Theater", "-$40.00")

        ),
        listOf(
            Transaction("2024-05-25", "Electronics Store", "-$200.00"),
            Transaction("2024-05-26", "Coffee Shop", "-$15.00"),
            Transaction("2024-05-27", "Bookstore", "-$30.00"),
            Transaction("2024-05-28", "Clothing Store", "-$75.00"),
            Transaction("2024-05-29", "Supermarket", "-$60.00"),
            Transaction("2024-06-03", "Car Mechanic", "-$145.00")
        ),
        listOf(
            Transaction("2024-05-20", "Pharmacy", "-$25.00"),
            Transaction("2024-05-21", "Restaurant", "-$80.00"),
            Transaction("2024-05-22", "Gas Station", "-$50.00"),


            ),
        listOf(
            Transaction("2024-06-05", "Hotel Booking", "-$300.00"),
            Transaction("2024-06-06", "Car Rental", "-$100.00"),
            Transaction("2024-06-07", "Flight Tickets", "-$500.00"),
            Transaction("2024-06-08", "Restaurant", "-$60.00"),
            Transaction("2024-06-09", "Tour Guide", "-$80.00"),
            Transaction("2024-05-24", "Concert Tickets", "-$150.00")
        )
    )
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
        val creditCardView = inflater.inflate(R.layout.item_debit_card,binding.cardContainer,false)
        creditCardView.findViewById<TextView>(R.id.card_number).text = "1234 5678 9012 3456"
        creditCardView.findViewById<TextView>(R.id.card_holder).text = "Radu Dumitrache"
        creditCardView.findViewById<TextView>(R.id.expiry_date).text = "12/24"
        creditCardView.findViewById<TextView>(R.id.card_value).text = "0"
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
                            value_container.text ="You have ${cardDetails[cardIndex].Sum.toString()} $ on this card"
                            val recyclerView = binding.transactionsRecyclerView
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            recyclerView.adapter = TransactionAdapter(transactionsList[cardIndex])
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateCardDetails(view: View, PcardDetails: CardDetails) {
        view.findViewById<TextView>(R.id.card_number).text = PcardDetails.cardNumber
        view.findViewById<TextView>(R.id.card_holder).text = PcardDetails.cardHolder
        view.findViewById<TextView>(R.id.expiry_date).text = PcardDetails.expiryDate
        view.findViewById<TextView>(R.id.card_value).text =  PcardDetails.Sum.toString()
    }
    private fun updateTransactions(recyclerView: RecyclerView, transactions: List<Transaction>) {
        recyclerView.adapter = TransactionAdapter(transactions)
    }
    private fun showAddCardDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_card, null)
        val cardNumberInput = dialogLayout.findViewById<EditText>(R.id.card_number_input)
        val cardHolderInput = dialogLayout.findViewById<EditText>(R.id.card_holder_input)
        val expiryDateInput = dialogLayout.findViewById<EditText>(R.id.expiry_date_input)

        with(builder) {
            setTitle("Add New Card")
            setView(dialogLayout)
            setPositiveButton("Add") { dialog, which ->
                val cardNumber = cardNumberInput.text.toString()
                val cardHolder = cardHolderInput.text.toString()
                val expiryDate = expiryDateInput.text.toString()
                if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && expiryDate.isNotEmpty()) {
                    val newCard = CardDetails(cardNumber, cardHolder, expiryDate,0f)
                    cardDetails.add(newCard)
                    transactionsList.toMutableList().add(listOf())
                    cardIndex = cardDetails.size - 1
                    updateCardDetails(binding.cardContainer.getChildAt(0), newCard)
                    updateTransactions(binding.transactionsRecyclerView, listOf())
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

    inner class TransactionAdapter(private val transactions: List<Transaction>) :
        RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transactions, parent, false)
            return TransactionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val transaction = transactions[position]
            holder.date.text = transaction.date
            holder.description.text = transaction.description
            holder.amount.text = transaction.amount

            // Apply coloring based on the transaction amount
            if (transaction.amount.startsWith("-")) {
                holder.amount.setTextColor(resources.getColor(R.color.negativeAmount, null))
            } else {
                holder.amount.setTextColor(resources.getColor(R.color.positiveAmount, null))
            }
        }

        override fun getItemCount() = transactions.size

        inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val date: TextView = view.findViewById(R.id.transaction_date)
            val description: TextView = view.findViewById(R.id.transaction_description)
            val amount: TextView = view.findViewById(R.id.transaction_amount)
        }
    }
}
