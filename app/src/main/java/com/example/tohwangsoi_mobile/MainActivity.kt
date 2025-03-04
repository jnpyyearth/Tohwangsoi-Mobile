package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Firestore", "🛠️ Checking Firestore Connection...111")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkFirestoreConnection()
    }
    private fun checkFirestoreConnection() {
        Log.d("Firestore", "🛠️ Checking Firestore Connection...")

        val db = FirebaseFirestore.getInstance()

        db.collection("test_connection").document("dummy").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val status = document.getString("status") ?: "No status field"
                    Log.d("Firestore11", "✅ Firestore connected successfully! Status: $status")
                } else {
                    Log.e("Firestore", "⚠️ Document does not exist!")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Firestore connection failed", e)
            }
    }


}