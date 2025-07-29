package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val nameEt = findViewById<EditText>(R.id.et_name)
        val emailEt = findViewById<EditText>(R.id.et_email)
        val passwordEt = findViewById<EditText>(R.id.et_password)
        val registerBtn = findViewById<Button>(R.id.btn_register)
        val loginTv = findViewById<TextView>(R.id.tv_login_here)

        registerBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Logout setelah berhasil daftar, agar user login ulang
                        auth.signOut()
                        Toast.makeText(this, "Pendaftaran berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AuthActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal daftar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginTv.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
