package com.example.appdev.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.appdev.databinding.FragmentGoalsBinding

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private val goalsViewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalsViewModel.goalTitle.observe(viewLifecycleOwner, Observer {
            binding.goalTitle.text = "Goal title: $it"
        })
        goalsViewModel.goalDescription.observe(viewLifecycleOwner, Observer {
            binding.goalDescription.text = "Goal description: $it"
        })
        goalsViewModel.dueDate.observe(viewLifecycleOwner, Observer {
            binding.dueDate.text = "Due date: $it"
        })
        goalsViewModel.amountSaved.observe(viewLifecycleOwner, Observer {
            binding.amountSaved.text = "Amount saved: $it"
        })
        goalsViewModel.remainingAmount.observe(viewLifecycleOwner, Observer {
            binding.remainingAmount.text = "Remaining amount: $it"
        })
        goalsViewModel.costTitle.observe(viewLifecycleOwner, Observer {
            binding.costTitle.text = "Cost title: $it"
        })
        goalsViewModel.amount.observe(viewLifecycleOwner, Observer {
            binding.amount.text = "Amount: $it"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
