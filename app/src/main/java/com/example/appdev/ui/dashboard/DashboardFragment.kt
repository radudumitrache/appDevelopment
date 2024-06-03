package com.example.appdev.ui.dashboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.appdev.R
import com.example.appdev.databinding.FragmentDashboardBinding
import com.example.appdev.util.CheckInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class DashboardFragment : Fragment() {
    private val MIN_SWIPE_DISTANCE = -200
    private var _binding: FragmentDashboardBinding? = null
    private val cardDetails = listOf(
        CardDetails("1234 5678 9012 3456", "John Doe", "12/24"),
        CardDetails("9876 5432 1098 7654", "Jane Smith", "11/23"),
        CardDetails("4567 8901 2345 6789", "Alice Johnson", "10/22")
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
                        }
                    }
                }
                v.performClick()
                return true
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateCardDetails(view: View, cardDetails: CardDetails) {
        view.findViewById<TextView>(R.id.card_number).text = cardDetails.cardNumber
        view.findViewById<TextView>(R.id.card_holder).text = cardDetails.cardHolder
        view.findViewById<TextView>(R.id.expiry_date).text = cardDetails.expiryDate
    }

    data class CardDetails(val cardNumber: String, val cardHolder: String, val expiryDate: String)
}