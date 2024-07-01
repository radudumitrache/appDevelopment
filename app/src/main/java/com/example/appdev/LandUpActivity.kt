package com.example.appdev

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity
import kotlinx.coroutines.launch

class LandUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_up)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val database = GoalSaverDatabase.getDatabase(this)


            val newUser = UserEntity(
                email = "john@example.com",
                password = "password123",
                profession = "Developer",
                age = "30",
                monthly_salary = 5000f,
                preffered_currency = "USD"
            )
            database.userDao().insert(newUser)

            val users = database.userDao().getAllUsers()
            users.forEach {
                println("User: ${it.email}, Profession: ${it.profession}")
            }
    }
}
