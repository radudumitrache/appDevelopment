package com.example.appdev

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val professionEditText = findViewById<EditText>(R.id.profession)
        val ageEditText = findViewById<EditText>(R.id.age)
        val monthly_salary_text = findViewById<EditText>(R.id.monthly_salary)
        val preffered_currency = findViewById<EditText>(R.id.preferred_currency)
        val registerButton = findViewById<Button>(R.id.RegisterButton)
        val backButton = findViewById<Button>(R.id.BackButton)
        registeruser("","","","",12.3f,"")


        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val profession = professionEditText.text.toString()
            val age = ageEditText.text.toString()
            val monthly_salary = monthly_salary_text.text.toString().toFloat()
            val preffered_currency = preffered_currency.text.toString()
            if (validateRegistration(email, password, profession, age)) {
                // Registration successful, show a success message
                registeruser(email,password,profession,age,monthly_salary,preffered_currency)
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity or perform other actions as needed
                intent = Intent(this,LandUpActivity::class.java)
                startActivity(intent)
                finish()
            // Close the registration activity
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
        password: String,
        profession: String,
        age: String
    ): Boolean {
        var user = GoalSaverDatabase.getDatabase(this).userDao().getUserByEmail(email)
        if (user!=null)
            return false
        return email.isNotEmpty() && password.isNotEmpty() && profession.isNotEmpty() && age.isNotEmpty()
    }
    private fun registeruser(email:String,
                             password: String,
                             profession: String,
                             age: String,
                             monthly_salary: Float,
                             preffered_currency: String)
    {
        var newUser : UserEntity = UserEntity(email=email, password = password, profession = profession, age = age, monthly_salary = monthly_salary, preffered_currency = preffered_currency)
        GoalSaverDatabase.getDatabase(this).userDao().insert(newUser)
    }
}