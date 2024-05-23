package com.example.appdev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    // Handle profile click
                    true
                }
                R.id.navigation_spendings -> {
                    // Handle spendings click
                    true
                }
                R.id.navigation_home -> {
                    // Handle home click
                    true
                }
                R.id.navigation_earnings -> {
                    // Handle earnings click
                    true
                }
                R.id.navigation_goals -> {
                    // Handle goals click
                    true
                }
                else -> false
            }
        }
    }
}
