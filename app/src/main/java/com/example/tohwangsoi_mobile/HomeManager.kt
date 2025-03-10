package com.example.tohwangsoi_mobile

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tohwangsoi_mobile.databinding.DialogAddMenuBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.IOException

class HomeManager : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var dialogBinding: DialogAddMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_home)

        // ปุ่มเพิ่มเมนู
        val buttonAdd = findViewById<FloatingActionButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            showAddMenuDialog()
        }

        // ปุ่ม Logout
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }

        database = Firebase.firestore
    }

    private fun logoutUser() {
        val auth = FirebaseAuth.getInstance()
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showAddMenuDialog() {
        val dialog = Dialog(this)
        val dialogBinding = DialogAddMenuBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root) // Set the content of the dialog

        dialog.setCancelable(true)

        dialogBinding.buttonSelectImage.setOnClickListener {
            openGallery()
        }

        dialogBinding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonConfirm.setOnClickListener {
            val menuName = dialogBinding.editTextMenuName.text.toString().trim()
            val selectedCategoryId = dialogBinding.radioGroupCategory.checkedRadioButtonId
            var category = ""

            when (selectedCategoryId) {
                R.id.radioAppetizer -> category = "เรียกน้ำย่อย"
                R.id.radioMainCourse -> category = "จานหลัก"
                R.id.radioDessert -> category = "ของหวาน"
                R.id.radioDrink -> category = "เครื่องดื่ม"
            }

            if (menuName.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกชื่อเมนู", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                Toast.makeText(this, "กรุณาเลือกประเภทเมนู", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save to Firestore
            val datamenu = hashMapOf(
                "MenuName" to menuName,
                "Category" to category,
                "Image" to dialogBinding.imageViewMenu.drawable, // Save image drawable reference
                "timestamp" to FieldValue.serverTimestamp(),
            )

            database.collection("Menu")
                .add(datamenu)
                .addOnSuccessListener {
                    Toast.makeText(this, "เมนูบันทึกสำเร็จ", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "ไม่สามารถบันทึกเมนูได้: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        dialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val selectedImageUri: Uri? = result.data?.data
                    try {
                        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                        dialogBinding.imageViewMenu.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
}