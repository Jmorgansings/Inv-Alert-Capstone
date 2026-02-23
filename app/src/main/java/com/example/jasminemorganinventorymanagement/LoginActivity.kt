package com.example.jasminemorganinventorymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.compose.ui.semantics.password

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                val (isSuccess, userId) = dbHelper.checkUser(username, password)

                if (isSuccess) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DataDisplayActivity::class.java)
                    intent.putExtra("USER_ID", userId) // Pass the logged-in user's ID
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                val success = dbHelper.addUser(username, password)
                if (success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed or user already exists.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter username and password to register", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
