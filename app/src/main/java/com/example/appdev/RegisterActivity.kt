package com.example.appdev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val emailEditText = findViewById<EditText>(R.id.email)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val professionEditText = findViewById<EditText>(R.id.profession)
        val ageEditText = findViewById<EditText>(R.id.age)
        val registerButton = findViewById<Button>(R.id.RegisterButton)
        val backButton = findViewById<Button>(R.id.BackButton)
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val profession = professionEditText.text.toString()
            val age = ageEditText.text.toString()

            if (validateRegistration(email, username, password, profession, age)) {
                // Registration successful, show a success message
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity or perform other actions as needed
                finish() // Close the registration activity
            }
            else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
        backButton.setOnClickListener {
            intent = Intent(this,LandUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun validateRegistration(
        email: String,
        username: String,
        password: String,
        profession: String,
        age: String
    ): Boolean {
        return email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && profession.isNotEmpty() && age.isNotEmpty()
    }
    private fun registeruser(email:String,
                             username: String,
                             password: String,
                             profession: String,
                             age: String)
    {

    }
}