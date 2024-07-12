package com.example.appdev.ui.goals

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.*

class GoalsFragment : Fragment() {

    private val goalViewModel: GoalsViewModel by viewModels()

    private lateinit var nonViableGoalsText: TextView

    companion object {
        lateinit var budgetImpact: TextView
        lateinit var prediction: TextView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goalsContainer: LinearLayout = view.findViewById(R.id.goalsContainer)
        val btnCreateGoal: Button = view.findViewById(R.id.btnCreateGoal)
        val btnCalculateImpact: Button = view.findViewById(R.id.btnCalculateImpact)
        val btnCheckViability: Button = view.findViewById(R.id.btnCheckViability)
        budgetImpact = view.findViewById(R.id.budgetImpact)
        prediction = view.findViewById(R.id.prediction)
        nonViableGoalsText = view.findViewById(R.id.nonViableGoals)

        goalViewModel.setGoalsFragment(this)
        goalViewModel.goals.observe(viewLifecycleOwner, Observer { goals ->
            goalsContainer.removeAllViews()

            if (goals.isNotEmpty()) {
                goals.forEach { goalDetails ->
                    val goalView = createGoalView(goalDetails, layoutInflater, null)
                    goalsContainer.addView(goalView)
                }
                goalsContainer.visibility = View.VISIBLE
            } else {
                goalsContainer.visibility = View.GONE
            }
        })

        btnCreateGoal.setOnClickListener {
            showCreateGoalDialog()
        }

        btnCalculateImpact.setOnClickListener {
            Log.d("GoalsFragment", "Clicked on Calculate Impact button.")
            updatePredictionText()
        }

        btnCheckViability.setOnClickListener {
            val averageMonthlySavings = AverageSavingsCalculator.averageSavingsPerMonth.toDouble()
            Log.d("GoalsFragment", "Average Monthly Savings: $averageMonthlySavings")
            val nonViableGoals = goalViewModel.checkGoalsViability(averageMonthlySavings)
            if (nonViableGoals.size == 1 && nonViableGoals[0] == "All goals are viable.") {
                nonViableGoalsText.text = getString(R.string.all_goals_viable)
                Toast.makeText(requireContext(), "All goals viable", Toast.LENGTH_SHORT).show()
            } else {
                nonViableGoalsText.text =
                    getString(R.string.non_viable_goals, nonViableGoals.joinToString(", "))
                Toast.makeText(requireContext(), nonViableGoalsText.text, Toast.LENGTH_SHORT).show()
            }
            nonViableGoalsText.visibility = View.VISIBLE
        }
    }

    private fun updatePredictionText() {
        val averageMonthlySavings = AverageSavingsCalculator.averageSavingsPerMonth.toDouble()
        Log.d("GoalsFragment", "Average Monthly Savings for Prediction: $averageMonthlySavings")
        val (moneyLeft, monthsToGoal) = goalViewModel.calculateBudgetImpact(averageMonthlySavings)
        budgetImpact.text = if (moneyLeft > 0) {
            getString(R.string.budget_left, "%.2f".format(moneyLeft))
        } else {
            getString(R.string.budget_deficit, "%.2f".format(-moneyLeft))
        }
        Toast.makeText(requireContext(), "Prediction updated", Toast.LENGTH_SHORT).show()
        prediction.text = getString(R.string.months_to_goal, monthsToGoal)
    }

    private fun showAddRelatedCostDialog(goalTitle: String) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_related_cost, null)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)
        val recurringSwitch = dialogView.findViewById<SwitchMaterial>(R.id.recurringSwitch)

        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.add_related_cost))
            .setView(dialogView).setPositiveButton(getString(R.string.add)) { dialog, _ ->
                val amountText = amountEditText.text.toString()
                val amount = if (amountText.isEmpty()) 0.0 else amountText.toDouble()
                val description = descriptionEditText.text.toString()
                val isRecurring = recurringSwitch.isChecked
                val relatedCost = GoalsViewModel.RelatedCost(0, description, amount, isRecurring)
                goalViewModel.addRelatedCost(goalTitle, relatedCost)
                dialog.dismiss()
                updatePredictionText()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun showCreateGoalDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_goal, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.etGoalTitle)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.etGoalDescription)
        val dueDateEditText = dialogView.findViewById<EditText>(R.id.etDueDate)
        val priceEditText = dialogView.findViewById<EditText>(R.id.etPrice)

        dueDateEditText.setOnClickListener {
            showDatePickerDialog(dueDateEditText)
        }

        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.create_new_goal))
            .setView(dialogView).setPositiveButton(getString(R.string.create)) { dialog, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val dueDate = dueDateEditText.text.toString()
                val priceText = priceEditText.text.toString()

                if (title.isNotBlank() && description.isNotBlank() && dueDate.isNotBlank() && priceText.isNotBlank()) {
                    try {
                        val price = priceText.toDouble()
                        if (price > 0) {
                            val goal = GoalsViewModel.GoalDetails(
                                0, title, description, dueDate, price, price
                            )
                            goalViewModel.addGoal(goal)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Price must be greater than zero.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid number for Price.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss()
                updatePredictionText()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun showDatePickerDialog(dueDateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dueDateEditText.setText(dateFormat.format(selectedDate.time))
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun createGoalView(
        goalDetails: GoalsViewModel.GoalDetails, inflater: LayoutInflater, container: ViewGroup?
    ): View {
        val view = inflater.inflate(R.layout.item_goal, container, false)
        val goalTitle: TextView = view.findViewById(R.id.goalTitle)
        val goalDescription: TextView = view.findViewById(R.id.goalDescription)
        val dueDate: TextView = view.findViewById(R.id.dueDate)
        val amount: TextView = view.findViewById(R.id.amountSaved)
        val remainingAmount: TextView = view.findViewById(R.id.remainingAmount)
        val btnAddRelatedCost: Button = view.findViewById(R.id.btnAddRelatedCost)
        val btnDeleteGoal: Button = view.findViewById(R.id.btnDeleteGoal)
        val relatedCostsContainer: LinearLayout = view.findViewById(R.id.relatedCostsContainer)

        goalTitle.text = getString(R.string.goal_title, goalDetails.title)
        goalDescription.text = getString(R.string.goal_description, goalDetails.description)
        dueDate.text = getString(R.string.due_date, goalDetails.dueDate)
        amount.text = getString(R.string.amount_saved, goalDetails.amount)
        remainingAmount.text = getString(R.string.remaining_amount, goalDetails.remainingAmount)

        btnAddRelatedCost.setOnClickListener {
            showAddRelatedCostDialog(goalDetails.title)
        }

        btnDeleteGoal.setOnClickListener {
            goalViewModel.deleteGoal(goalDetails.goalId)
            updatePredictionText()
        }

        relatedCostsContainer.removeAllViews()
        goalDetails.relatedCosts.forEach { relatedCost ->
            val relatedCostView = createRelatedCostView(
                relatedCost, inflater, relatedCostsContainer, goalDetails.title
            )
            relatedCostsContainer.addView(relatedCostView)
        }

        return view
    }

    private fun createRelatedCostView(
        relatedCost: GoalsViewModel.RelatedCost,
        inflater: LayoutInflater,
        container: ViewGroup?,
        goalTitle: String
    ): View {
        val view = inflater.inflate(R.layout.item_related_cost, container, false)
        val costTitle: TextView = view.findViewById(R.id.tvCostTitle)
        val costAmount: TextView = view.findViewById(R.id.tvAmount)
        val btnDeleteRelatedCost: Button = view.findViewById(R.id.btnDeleteRelatedCost)

        costTitle.text = relatedCost.title
        costAmount.text = relatedCost.amount.toString()

        btnDeleteRelatedCost.setOnClickListener {
            goalViewModel.removeRelatedCost(goalTitle, relatedCost)
            updatePredictionText()
        }

        return view
    }
}
