package com.example.appdev.ui.goals

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appdev.R
import java.text.SimpleDateFormat
import java.util.*

class CreateGoalFragment : Fragment() {

    private val goalViewModel: CreateGoalViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.dialog_create_goal, container, false)


        val etGoalTitle = view.findViewById<EditText>(R.id.etGoalTitle)
        val etGoalDescription = view.findViewById<EditText>(R.id.etGoalDescription)
        val etDueDate = view.findViewById<EditText>(R.id.etDueDate)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateGoal)


        etDueDate.setOnClickListener {
            showDatePickerDialog(etDueDate)
        }

        btnCreate.setOnClickListener {
            val title = etGoalTitle.text.toString()
            val description = etGoalDescription.text.toString()
            val date = etDueDate.text.toString()
            val price = etPrice.text.toString()

            if (title.isNotBlank() && description.isNotBlank() && date.isNotBlank() && price.isNotBlank()) {
                try {
                    val priceValue = price.toDouble()
                    if (priceValue > 0) {
                        goalViewModel.createGoal(title, description, date, price)
                        // Navigate to dashboard or other appropriate action
                    } else {
                        Toast.makeText(requireContext(), "Price must be greater than zero.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Please enter a valid number for Price.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    private fun showDatePickerDialog(dueDateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dueDateEditText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }

}
