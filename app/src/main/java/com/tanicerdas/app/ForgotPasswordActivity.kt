package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.et_email)
        val resetBtn = findViewById<Button>(R.id.btn_reset_password)
        val backTv = findViewById<TextView>(R.id.tv_back_to_login)

        resetBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Link reset dikirim ke email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AuthActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal mengirim: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        backTv.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
