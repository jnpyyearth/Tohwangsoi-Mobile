package com.example.tohwangsoi_mobile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.databinding.DialogAddMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class HomeManager : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_home)

        //firebase
        FirebaseApp.initializeApp(this)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_manager)
        val navView: NavigationView = findViewById(R.id.nav_view_manager)
        val toolbar: Toolbar = findViewById(R.id.toolbar_manager)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerView = findViewById(R.id.recyclerView)

        // ตั้งค่า Toolbar ให้เป็น ActionBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // สร้าง ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // หา Header View ของ NavigationView
        val headerView = navView.getHeaderView(0)
        val btnLogout: Button = headerView.findViewById(R.id.btnLogout)
        // ตั้งค่าปุ่ม Logout
        btnLogout.setOnClickListener {
            logoutUser()
        }

        buttonAdd.setOnClickListener {
            replaceFragment(AddmenuFragment())
        }

        val buttonAdd = findViewById<FloatingActionButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            showAddMenuDialog()
        }

        database = Firebase.firestore

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_addmenu -> replaceFragment(AddmenuFragment())
                R.id.nav_menu -> replaceFragment(MenuFragment())
                R.id.nav_addmanager -> replaceFragment(AddManagerFragment())
                R.id.nav_order -> replaceFragment(OrderFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_manager)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun logoutUser() {
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        FirebaseAuth.getInstance().signOut()
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

            val imageUrl = dialogBinding.editTextImageLink.text.toString().trim() // รับ URL ของรูปภาพ

            if (menuName.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกชื่อเมนู", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                Toast.makeText(this, "กรุณาเลือกประเภทเมนู", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUrl.isEmpty()) {
                Toast.makeText(this, "กรุณากรอกลิงก์รูปภาพ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save to Firestore
            val datamenu = hashMapOf(
                "MenuName" to menuName,
                "Category" to category,
                "Image" to imageUrl, // Save the image URL
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        // ✅ แสดง `buttonAdd` และ `recyclerView` เฉพาะที่หน้า AddMenuFragment
        if (fragment is AddmenuFragment) {
            buttonAdd.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        } else {
            buttonAdd.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

}