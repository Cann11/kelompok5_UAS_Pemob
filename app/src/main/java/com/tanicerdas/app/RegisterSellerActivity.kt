package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterSellerActivity : AppCompatActivity() {

    private lateinit var etStoreName: EditText
    private lateinit var etStoreLocation: EditText
    private lateinit var etPhone: EditText
    private lateinit var etBio: EditText
    private lateinit var btnRegisterSeller: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_seller)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        etStoreName = findViewById(R.id.etStoreName)
        etStoreLocation = findViewById(R.id.etStoreLocation)
        etPhone = findViewById(R.id.etPhone)
        etBio = findViewById(R.id.etBio)
        btnRegisterSeller = findViewById(R.id.btnRegisterSeller)

        btnRegisterSeller.setOnClickListener {
            registerSeller()
        }
    }

    private fun registerSeller() {
        val storeName = etStoreName.text.toString().trim()
        val storeLocation = etStoreLocation.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val bio = etBio.text.toString().trim()

        if (storeName.isEmpty() || storeLocation.isEmpty() || phone.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        // Update ke koleksi users
        val userRef = firestore.collection("users").document(userId)
        val data = mapOf(
            "storeName" to storeName,
            "storeLocation" to storeLocation,
            "phone" to phone,
            "bio" to bio,
            "role" to "seller"
        )

        userRef.update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                // Kembali ke ProfileFragment
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigateTo", "profile") // untuk navigasi ulang ke profil
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mendaftar: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
