package com.example.appdev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdev.R.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        val usernameEditText = findViewById<EditText>(id.username)
        val passwordEditText = findViewById<EditText>(id.password)
        val loginButton = findViewById<Button>(id.loginButton)
        val backButton = findViewById<Button>(id.BackButtonLogin)
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(username, password)) {
                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
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

    private fun validateLogin(username: String, password: String): Boolean {

        return username == "user" && password == "password"
    }
}