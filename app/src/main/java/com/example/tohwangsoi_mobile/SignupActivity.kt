package com.example.tohwangsoi_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.example.tohwangsoi_mobile.databinding.ActivitySignupBinding
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupViews()
    }

    private fun setupViews() {
        binding.btnSignup.setOnClickListener {
            if (validateInputs()) {
                // Perform signup operation
                performSignup()
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate full name
        val fullName = binding.etFullName.text.toString().trim()
        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Please enter your full name"
            isValid = false
        } else {
            binding.tilFullName.error = null
        }

        // Validate email
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = "Please enter your email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Please enter a valid email address"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        // Validate password
        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            binding.tilPassword.error = "Please enter a password"
            isValid = false
        } else if (password.length < 8) {
            binding.tilPassword.error = "Password must be at least 8 characters"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        // Validate confirm password
        val confirmPassword = binding.etConfirmPassword.text.toString()
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        // Validate terms checkbox
        if (!binding.cbTerms.isChecked) {
            Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT)
                .show()
            isValid = false
        }

        return isValid
    }

    private fun performSignup() {
        // Show loading indicator
        showLoading(true)

        val fullName = binding.etFullName.text
        val email = binding.etEmail.text
        val password = binding.etPassword.text

        // Create user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    // Create user data for Firestore
                    val user = hashMapOf(
                        "userId" to userId,
                        "fullName" to fullName.toString().trim(),
                        "email" to email.toString().trim(),
                        "password" to password.toString().trim(),
                        "timestamp" to FieldValue.serverTimestamp(),
                        "role" to "customer"
                    )

                    // Save user data in Firestore
                    if (userId != null) {
                        database.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                showLoading(false)
                                Toast.makeText(this, "User Registration successfully", Toast.LENGTH_SHORT).show()

                                // Navigate to Login Activity
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                showLoading(false)
                                Toast.makeText(this, "Failed to add new user: ${e.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(show: Boolean) {
        binding.signUpProgress.visibility = if (show) View.VISIBLE else View.GONE
    }
}
