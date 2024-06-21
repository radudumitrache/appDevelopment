package com.example.appdev.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.R

class GoalsFragment : Fragment() {

    private val goalViewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        val relatedCostsContainer: LinearLayout = view.findViewById(R.id.relatedCostsContainer)

        val btnAddRelatedCost = view.findViewById<Button>(R.id.btnAddRelatedCost)

        val goalTitle = view.findViewById<TextView>(R.id.goalTitle)
        val goalDescription = view.findViewById<TextView>(R.id.goalDescription)
        val dueDate = view.findViewById<TextView>(R.id.dueDate)
        val amount = view.findViewById<TextView>(R.id.amountSaved)
        val remainingAmount = view.findViewById<TextView>(R.id.remainingAmount)

        goalViewModel.goalDetails.observe(viewLifecycleOwner, Observer { goalDetails ->
            goalTitle.text = "Title: ${goalDetails.title}"
            goalDescription.text = "Description: ${goalDetails.description}"
            dueDate.text = "Due date: ${goalDetails.dueDate}"
            amount.text = "Amount: ${goalDetails.amount}"
            remainingAmount.text = "Remaining: ${goalDetails.remainingAmount}"
        })


        goalViewModel.relatedCosts.observe(viewLifecycleOwner, Observer { relatedCosts ->
            relatedCostsContainer.removeAllViews()
            relatedCosts.forEach { relatedCostDetails ->
                val cardView = createRelatedCostCard(relatedCostDetails, inflater, container)
                relatedCostsContainer.addView(cardView)
            }
        })

        btnAddRelatedCost.setOnClickListener {
            showAddRelatedCostDialog()
        }

        return view
    }

    private fun showAddRelatedCostDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_related_cost, null)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Transaction")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val amountText = amountEditText.text.toString()
                val amount = if (amountText.isEmpty()) 0.0 else amountText.toDouble()
                val description = descriptionEditText.text.toString()
                goalViewModel.addRelatedCost(GoalsViewModel.RelatedCost(description, amount))
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun createRelatedCostCard(
        relatedCostDetails: GoalsViewModel.RelatedCost,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val card = inflater.inflate(R.layout.item_related_cost, container, false)
        val costTitle = card.findViewById<TextView>(R.id.tvCostTitle)
        val amount = card.findViewById<TextView>(R.id.tvAmount)

        costTitle.text = relatedCostDetails.title
        amount.text = relatedCostDetails.amount.toString()

        return card
    }
}
