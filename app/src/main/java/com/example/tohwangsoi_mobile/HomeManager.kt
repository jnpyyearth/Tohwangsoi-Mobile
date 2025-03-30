package com.example.tohwangsoi_mobile

import AddmenuFragment
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class HomeManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_home)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_manager)
        val navView: NavigationView = findViewById(R.id.nav_view_manager)
        val toolbar: Toolbar = findViewById(R.id.toolbar_manager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val headerView = navView.getHeaderView(0)
        val btnLogout: Button = headerView.findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            logoutUser()
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_addmenu -> replaceFragment(AddmenuFragment()) // ✅ เปิดหน้าเพิ่มเมนู
//                R.id.nav_menu -> replaceFragment(MenuFragment())
                R.id.nav_addmanager -> replaceFragment(AddManagerFragment())
                R.id.nav_order-> replaceFragment(OrderFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // โหลด Fragment เริ่มต้นเมื่อเปิดแอป
        if (savedInstanceState == null) {
            replaceFragment(AddmenuFragment()) // หรือ Fragment ที่ต้องการให้แสดงเป็นหน้าแรก
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun logoutUser() {
        val auth = FirebaseAuth.getInstance()
        val googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )

        // ออกจากระบบ Firebase
        auth.signOut()

        // ออกจากระบบ Google
        googleSignInClient.signOut().addOnCompleteListener {
            // เคลียร์ข้อมูล SharedPreferences
            val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            // แจ้งเตือนและกลับไปหน้า Login
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
