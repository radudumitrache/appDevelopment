package com.example.appdev.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appdev.R

class CreateGoalFragment : Fragment() {

    private val goalViewModel: CreateGoalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_create_goal, container, false)

        // Get references to the EditText fields and the button
        val etGoalTitle = view.findViewById<EditText>(R.id.etGoalTitle)
        val etGoalDescription = view.findViewById<EditText>(R.id.etGoalDescription)
        val etDueDate = view.findViewById<EditText>(R.id.etDueDate)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etMonthlySavings = view.findViewById<EditText>(R.id.etMonthlySavings)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateGoal)

        // Set the button click listener
        btnCreate.setOnClickListener {
            val title = etGoalTitle.text.toString()
            val description = etGoalDescription.text.toString()
            val date = etDueDate.text.toString()
            val price = etPrice.text.toString()
            val monthlySavings = etMonthlySavings.text.toString()

            goalViewModel.createGoal(title, description, date, price, monthlySavings)
            // navigate to dashboard?
        }

        return view
    }
}
