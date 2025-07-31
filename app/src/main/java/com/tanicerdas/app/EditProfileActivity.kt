package com.tanicerdas.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etBio: EditText
    private lateinit var btnSave: Button
    private lateinit var btnUbahFoto: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var photoUrl: String? = null // URL dari gambar profil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPhone = findViewById(R.id.et_phone)
        etAddress = findViewById(R.id.et_address)
        etBio = findViewById(R.id.et_bio)
        btnSave = findViewById(R.id.btn_save)
        btnUbahFoto = findViewById(R.id.btn_ubah_foto)

        // Load data profil dari Firestore
        loadUserProfile()

        // Tombol Ubah Foto
        btnUbahFoto.setOnClickListener {
            showInputImageDialog()
        }

        // Tombol Simpan
        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    etName.setText(doc.getString("name"))
                    etEmail.setText(doc.getString("email"))
                    etPhone.setText(doc.getString("phone"))
                    etAddress.setText(doc.getString("address"))
                    etBio.setText(doc.getString("bio"))

                    photoUrl = doc.getString("photoUrl")
                    Glide.with(this)
                        .load(photoUrl ?: R.drawable.ic_profil)
                        .circleCrop()
                        .into(imgProfile)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showInputImageDialog() {
        val input = EditText(this)
        input.hint = "https://...jpg/png"
        input.setText(photoUrl ?: "")

        AlertDialog.Builder(this)
            .setTitle("Masukkan URL Foto Profil")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val url = input.text.toString().trim()
                if (url.isNotEmpty()) {
                    photoUrl = url
                    Glide.with(this)
                        .load(photoUrl)
                        .circleCrop()
                        .into(imgProfile)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val updatedData = mapOf(
            "name" to etName.text.toString().trim(),
            "phone" to etPhone.text.toString().trim(),
            "address" to etAddress.text.toString().trim(),
            "bio" to etBio.text.toString().trim(),
            "photoUrl" to (photoUrl ?: "")
        )

        firestore.collection("users").document(uid)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
            }
    }
}
