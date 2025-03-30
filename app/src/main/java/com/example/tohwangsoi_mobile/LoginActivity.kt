package com.example.tohwangsoi_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tohwangsoi_mobile.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)


        if (isLoggedIn) {
            val userId = sharedPref.getString("userId", null)
            if (userId != null) {
                // ตรวจสอบ email ใน Firestore และไปที่หน้าหลัก
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val fullName = document.getString("fullName") ?: "Unknown"
                            val role = document.getString("role") ?: "customer"
                            val email = sharedPref.getString("email", "")

                            val intent = when (role) {
                                "manager" -> Intent(this, HomeManager::class.java)
                                else -> Intent(this, MainActivity::class.java)
                            }

                            intent.putExtra("USER_NAME", fullName)
                            intent.putExtra("USER_EMAIL", email)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        } else {
            // หากไม่ได้ล็อกอินให้แสดงหน้า Login
            binding.btnLogin.setOnClickListener {
                performLogin()
            }
        }

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.btnGoogleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Please enter a valid email"
            return
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Please enter your password"
            return
        } else {
            binding.tilPassword.error = null
        }

        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Logging in..."
        binding.progressBar.visibility = View.VISIBLE

        // เช็ค email ใน Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // ตรวจสอบ email ใน Firestore
                        db.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val fullName = document.getString("fullName") ?: "Unknown"
                                    val role = document.getString("role") ?: "customer"

                                    val intent = when (role) {
                                        "manager" -> Intent(this, HomeManager::class.java)
                                        else -> Intent(this, MainActivity::class.java)
                                    }

                                    intent.putExtra("USER_NAME", fullName)
                                    intent.putExtra("USER_EMAIL", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // ถ้าไม่มีข้อมูลใน Firestore
                                    showToast("User data not found in Firestore")
                                }
                            }
                            .addOnFailureListener { e ->
                                showToast("Failed to load user info: ${e.message}")
                            }
                    }
                } else {
                    showToast("Login failed: ${task.exception?.message}")
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = getString(R.string.login)
                    binding.progressBar.visibility = View.GONE
                }
            }


        //login ค้างไว้  ซนนนนนนนนนนน
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // บันทึกสถานะการล็อกอิน
                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.putString("userId", userId)
                        editor.putString("email", email)
                        editor.apply()

                        // ตรวจสอบ email ใน Firestore
                        db.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val fullName = document.getString("fullName") ?: "Unknown"
                                    val role = document.getString("role") ?: "customer"

                                    val intent = when (role) {
                                        "manager" -> Intent(this, HomeManager::class.java)
                                        else -> Intent(this, MainActivity::class.java)
                                    }

                                    intent.putExtra("USER_NAME", fullName)
                                    intent.putExtra("USER_EMAIL", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // ถ้าไม่มีข้อมูลใน Firestore
                                    showToast("User data not found in Firestore")
                                }
                            }
                            .addOnFailureListener { e ->
                                showToast("Failed to load user info: ${e.message}")
                            }
                    }
                } else {
                    showToast("Login failed: ${task.exception?.message}")
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = getString(R.string.login)
                    binding.progressBar.visibility = View.GONE
                }
            }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google login successful!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}