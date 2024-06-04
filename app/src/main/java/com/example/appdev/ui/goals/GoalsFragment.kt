package com.example.appdev.ui.goals
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appdev.R
import com.google.android.material.textfield.TextInputEditText

class GoalFragment : Fragment() {

    private val goalViewModel: CreateGoalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)

        // Get references to the EditText fields and the button
        val etGoalTitle = view.findViewById<TextInputEditText>(R.id.etGoalTitle)
        val etGoalDescription = view.findViewById<TextInputEditText>(R.id.etGoalDescription)
        val etDueDate = view.findViewById<TextInputEditText>(R.id.etDueDate)
        val etPrice = view.findViewById<TextInputEditText>(R.id.etPrice)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateGoal)

        // Set the button click listener
        btnCreate.setOnClickListener {
            val title = etGoalTitle.text.toString()
            val description = etGoalDescription.text.toString()
            val date = etDueDate.text.toString()
            val price = etPrice.text.toString()

            goalViewModel.updateGoal(title, description, date, price)
            // Optionally, show a confirmation message or navigate to another screen
        }

        return view
    }
}
