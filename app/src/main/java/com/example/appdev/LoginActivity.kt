package com.example.appdev
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast

class LoginActivity : ComponentActivity(){
    private var username:String? = null
    private var password:String? = null
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USERNAME_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }
    private lateinit var sharedpreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val username_input = findViewById<EditText>(R.id.username_input)
        val password_input = findViewById<EditText>(R.id.password_input)
        val login_button = findViewById<Button>(R.id.login_button)
        username = sharedpreferences.getString("EMAIL_KEY", null)
        password = sharedpreferences.getString("PASSWORD_KEY", null)
        login_button.setOnClickListener {
            if (TextUtils.isEmpty(username_input.text.toString()) && TextUtils.isEmpty(password_input.text.toString()))
            {
                Toast.makeText(this,"Please Enter Email and Password",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val editor = sharedpreferences.edit()
                editor.putString(USERNAME_KEY,username_input.text.toString())
                editor.putString(PASSWORD_KEY,password_input.text.toString())
                editor.apply()
                val i = Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
            }

        }


    }
    override fun onStart() {
        super.onStart()
        if (username != null && password != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

}