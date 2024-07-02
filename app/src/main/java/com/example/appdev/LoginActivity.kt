package com.example.appdev

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.appdev.R.*
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)


        val emailEditText = findViewById<EditText>(id.email)
        val passwordEditText = findViewById<EditText>(id.password)
        val loginButton = findViewById<Button>(id.loginButton)
        val backButton = findViewById<Button>(id.BackButtonLogin)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                // Navigate to MainActivity
                var user_with_mail = GoalSaverDatabase.getDatabase(this).userDao().getUserByEmail(email)
                val intent = Intent(this, MainActivity::class.java).apply {
                   putExtra("USER",user_with_mail)
                }
                startActivity(intent)
                finish()
            } else {
                // Show error message
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
        backButton.setOnClickListener {
            intent = Intent(this,LandUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {

        var user_with_mail = GoalSaverDatabase.getDatabase(this).userDao().getUserByEmail(email)
        if (user_with_mail != null) {
            if (user_with_mail.password == password)
                return true
        }
        return false
    }
}