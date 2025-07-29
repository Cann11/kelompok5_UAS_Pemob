package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Cek apakah user sudah login
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Kalau belum login, arahkan ke AuthActivity
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return
        }

        // Kalau sudah login, tampilkan layout utama
        setContentView(R.layout.activity_main)
        // Menampilkan fragment awal (Beranda)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_education -> EducationFragment()
                R.id.nav_marketplace -> MarketplaceFragment()
                R.id.nav_consultation -> ConsultationFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }

        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "Edukasi") {
            bottomNav.selectedItemId = R.id.nav_education
        }



    }
}

