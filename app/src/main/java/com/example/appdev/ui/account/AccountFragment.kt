package com.example.appdev.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appdev.LoginActivity
import com.example.appdev.MainActivity
import com.example.appdev.R
import com.example.appdev.database.entities.UserEntity

class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val editEmailButton: EditText = view.findViewById(R.id.editEmailButton)
        val editPasswordButton: EditText = view.findViewById(R.id.editPasswordButton)
        val editTextProfession: EditText = view.findViewById(R.id.editTextText2)
        val editTextCurrency: EditText = view.findViewById(R.id.editTextText3)
        val changeInformationButton: Button = view.findViewById(R.id.ChangeInformationButton)
        val goToFriendsButton: Button = view.findViewById(R.id.goToFriendsButton)
        val logoutButton: Button = view.findViewById(R.id.logout_button)

        // Load user data
        val userId = MainActivity.logged_user!!.user_id // Assuming you have user_id available in MainActivity
        viewModel.loadUser(userId)

        viewModel.user.observe(viewLifecycleOwner, { user ->
            editEmailButton.setText(user.email)
            editPasswordButton.setText(user.password)
            editTextProfession.setText(user.profession)
            editTextCurrency.setText(user.preffered_currency)
        })

        changeInformationButton.setOnClickListener {
            if (MainActivity.logged_user != null) {
                val updatedUser = UserEntity(
                    user_id = userId,
                    email = editEmailButton.text.toString(),
                    password = editPasswordButton.text.toString(),
                    profession = editTextProfession.text.toString(),
                    dateOfBirth = MainActivity.logged_user!!.dateOfBirth, // Placeholder, add actual logic if needed
                    monthly_salary = 0f, // Placeholder, add actual logic if needed
                    preffered_currency = editTextCurrency.text.toString()
                )
                viewModel.updateUser(updatedUser)
            }
        }

        goToFriendsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_account_to_navigation_friends)
        }

        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        // Clear the logged-in user state
        MainActivity.logged_user = null

        // Navigate back to the LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
