package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CreateCommunityActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etKategori: EditText
    private lateinit var etFoto: EditText
    private lateinit var btnSimpan: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)

        etNama = findViewById(R.id.etNama)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        etKategori = findViewById(R.id.etKategori)
        etFoto = findViewById(R.id.etFoto)
        btnSimpan = findViewById(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            simpanKomunitas()
        }
    }

    private fun simpanKomunitas() {
        val nama = etNama.text.toString().trim()
        val deskripsi = etDeskripsi.text.toString().trim()
        val kategori = etKategori.text.toString().trim()
        val fotoUrl = etFoto.text.toString().trim()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (nama.isEmpty() || deskripsi.isEmpty() || kategori.isEmpty() || fotoUrl.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val id = UUID.randomUUID().toString()
        val komunitas = hashMapOf(
            "id" to id,
            "nama" to nama,
            "deskripsi" to deskripsi,
            "kategori" to kategori,
            "foto" to fotoUrl,
            "dibuat_oleh" to uid,
            "tanggal_dibuat" to FieldValue.serverTimestamp(),
            "anggota" to listOf(uid),
            "status" to "Aktif"
        )

        db.collection("komunitas").document(id)
            .set(komunitas)
            .addOnSuccessListener {
                Toast.makeText(this, "Komunitas berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                // Pindah ke CommunityActivity atau kembali ke fragment list
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigate_to", "community") // kalau pakai bottom nav
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan komunitas", Toast.LENGTH_SHORT).show()
            }
    }
}
