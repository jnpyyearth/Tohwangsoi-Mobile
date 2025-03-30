package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.adapter.ManagerAdapter
import com.example.tohwangsoi_mobile.databinding.FragmentAddManagerBinding
import com.example.tohwangsoi_mobile.model.Manager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue


class AddManagerFragment : Fragment() {

    private lateinit var binding: FragmentAddManagerBinding
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    //ซนนนนนนนนนนนนนนนนนน
    private lateinit var recyclerView: RecyclerView
    private lateinit var managerAdapter: ManagerAdapter
    private var managerList = mutableListOf<Manager>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddManagerBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        buttonAdd = binding.fabAddManager
        buttonAdd.setOnClickListener {
            showAddManagerDialog()
        }

        // กำหนดค่า RecyclerView
        recyclerView = binding.recyclerViewmanager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchManagersFromFirestore() // โหลดข้อมูลจาก Firestore




        return binding.root
    }

    private fun fetchManagersFromFirestore() {
        database.collection("users")
            .whereEqualTo("role", "manager") // ดึงข้อมูลเฉพาะ role = "manager"
            .get()
            .addOnSuccessListener { result ->
                managerList.clear()
                for (document in result) {
                    val manager = document.toObject(Manager::class.java)
                    managerList.add(manager)
                }
                managerAdapter = ManagerAdapter(managerList)
                recyclerView.adapter = managerAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun showAddManagerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_manager, null)

        // ตั้งค่า Dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .setPositiveButton("Add") { _, _ ->
                val fullName = dialogView.findViewById<TextInputEditText>(R.id.et_full_name).text.toString()
                val email = dialogView.findViewById<TextInputEditText>(R.id.et_email).text.toString()
                val password = dialogView.findViewById<TextInputEditText>(R.id.et_password).text.toString()

                // ตรวจสอบข้อมูลและบันทึกข้อมูล
                if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    // ลงทะเบียนผู้ใช้ด้วย Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid

                                // สร้างข้อมูลผู้ใช้เพื่อบันทึกใน Firestore
                                val user = hashMapOf(
                                    "userId" to userId,
                                    "fullName" to fullName.trim(),
                                    "email" to email.trim(),
                                    "password" to password.trim(),
                                    "timestamp" to FieldValue.serverTimestamp(),
                                    "role" to "manager" // หรือ role อื่นๆ ที่คุณต้องการ
                                )

                                // บันทึกข้อมูลใน Firestore
                                if (userId != null) {
                                    database.collection("users").document(userId)
                                        .set(user)
                                        .addOnSuccessListener {
                                            fetchManagersFromFirestore()
                                            Toast.makeText(context, "User registration successful", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            // แสดงข้อความเมื่อเกิดข้อผิดพลาด
                                            Toast.makeText(context, "Failed to add new user: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                // ถ้าการลงทะเบียนไม่สำเร็จ
                                Toast.makeText(context, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // ถ้าผู้ใช้กรอกข้อมูลไม่ครบ
                    Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}

