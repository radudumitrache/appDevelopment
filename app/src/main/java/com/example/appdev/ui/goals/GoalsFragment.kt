package com.example.appdev.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R
import com.example.appdev.database.entities.GoalEntity
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.Locale

class GoalsFragment : Fragment() {

    private val goalViewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        val goalsContainer: LinearLayout = view.findViewById(R.id.goalsContainer)
        val btnAddRelatedCost: Button = view.findViewById(R.id.btnAddRelatedCost)
        val btnCreateGoal: Button = view.findViewById(R.id.btnCreateGoal)
        val btnCalculateImpact: Button = view.findViewById(R.id.btnCalculateImpact)

        val budgetImpact: TextView = view.findViewById(R.id.budgetImpact)
        val prediction: TextView = view.findViewById(R.id.prediction)

        goalViewModel.goals.observe(viewLifecycleOwner, Observer { goals ->
            goalsContainer.removeAllViews()
            goals.forEach { goal ->
                val goalView = createGoalView(goal, inflater, container)
                goalsContainer.addView(goalView)
            }
        })

        btnAddRelatedCost.setOnClickListener {
            showAddRelatedCostDialog()
        }

        btnCreateGoal.setOnClickListener {
            showCreateGoalDialog()
        }

        btnCalculateImpact.setOnClickListener {
            val impact = goalViewModel.calculateBudgetImpact()
            val monthsToGoal = goalViewModel.predictMonthsToGoal()
            budgetImpact.text = getString(R.string.budget_impact, impact)
            prediction.text = getString(R.string.months_to_goal, monthsToGoal)
        }

        return view
    }

    private fun showAddRelatedCostDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_related_cost, null)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)
        val recurringSwitch = dialogView.findViewById<SwitchMaterial>(R.id.recurringSwitch)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_related_cost))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.add)) { dialog, _ ->
                val amountText = amountEditText.text.toString()
                val amount = if (amountText.isEmpty()) 0.0 else amountText.toDouble()
                val description = descriptionEditText.text.toString()
                val isRecurring = recurringSwitch.isChecked
                goalViewModel.addRelatedCost(GoalsViewModel.RelatedCost(description, amount, isRecurring))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showCreateGoalDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_goal, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.etGoalTitle)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.etGoalDescription)
        val dueDateEditText = dialogView.findViewById<EditText>(R.id.etDueDate)
        val priceEditText = dialogView.findViewById<EditText>(R.id.etPrice)
        val monthlySavingsEditText = dialogView.findViewById<EditText>(R.id.etMonthlySavings)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.create_new_goal))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.create)) { dialog, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val dueDate = dueDateEditText.text.toString()
                val price = priceEditText.text.toString().toDouble()
                val monthlySavings = monthlySavingsEditText.text.toString().toDouble()
                val goal = GoalsViewModel.GoalDetails(title, description, dueDate, price, 0.0, monthlySavings)
                goalViewModel.addGoal(goal)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun createGoalView(goal: GoalEntity, inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.item_goal, container, false)
        val goalTitle: TextView = view.findViewById(R.id.goalTitle)
        val goalDescription: TextView = view.findViewById(R.id.goalDescription)
        val dueDate: TextView = view.findViewById(R.id.dueDate)
        val amount: TextView = view.findViewById(R.id.amountSaved)
        val remainingAmount: TextView = view.findViewById(R.id.remainingAmount)

        goalTitle.text = getString(R.string.goal_title, goal.title)
        goalDescription.text = getString(R.string.goal_description, goal.description)
        dueDate.text = getString(R.string.due_date, SimpleDateFormat("dd/MM/yyyy", Locale.US).format(goal.due_date))
        amount.text = getString(R.string.amount_saved, goal.current_amount.toFloat())
        remainingAmount.text = getString(R.string.remaining_amount, goal.target_amount.toFloat() - goal.current_amount.toFloat())

        return view
    }
}
