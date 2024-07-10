package com.example.appdev
import android.app.DatePickerDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var birthdayTextView: TextView
    private lateinit var selectBirthdayButton: ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val professionEditText = findViewById<EditText>(R.id.profession)
        val monthly_salary_text = findViewById<EditText>(R.id.monthly_salary)
        val preffered_currency = findViewById<EditText>(R.id.preferred_currency)
        val registerButton = findViewById<Button>(R.id.RegisterButton)
        val backButton = findViewById<Button>(R.id.BackButton)
        birthdayTextView = findViewById<TextView>(R.id.birthdayTextView)
        selectBirthdayButton = findViewById<ImageButton>(R.id.selectBirthdayButton)
        selectBirthdayButton.setOnClickListener {
            showDatePickerDialog()
        }
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val profession = professionEditText.text.toString()
            val monthly_salary = monthly_salary_text.text.toString().toFloat()
            val preffered_currency = preffered_currency.text.toString()
            val birthday = birthdayTextView.text.toString()
            if (validateRegistration(email, password,birthday, profession)) {
                // Registration successful, show a success message
                registeruser(email,password,profession,parseDate(birthday),monthly_salary,preffered_currency)
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
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            birthdayTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    // Utility function to parse date string to Date object
    private fun parseDate(dateStr: String): Date {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.parse(dateStr) ?: Date()
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
                             dateOfBirth : Date,
                             monthly_salary: Float,
                             preffered_currency: String)
    {
        var newUser : UserEntity = UserEntity(email=email, password = password, profession = profession, dateOfBirth = dateOfBirth, monthly_salary = monthly_salary, preffered_currency = preffered_currency)
        GoalSaverDatabase.getDatabase(this).userDao().insert(newUser)
    }
}