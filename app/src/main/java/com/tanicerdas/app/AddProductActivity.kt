package com.tanicerdas.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etStok: EditText
    private lateinit var btnTambahGambar: Button
    private lateinit var imgPreview: ImageView
    private lateinit var btnSimpan: Button

    private var imageUrl: String = ""
    private var isEditMode = false
    private var productId: String = ""

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etNama = findViewById(R.id.et_nama)
        etHarga = findViewById(R.id.et_harga)
        etDeskripsi = findViewById(R.id.et_deskripsi)
        etStok = findViewById(R.id.et_stok)
        btnTambahGambar = findViewById(R.id.btnTambahGambar)
        imgPreview = findViewById(R.id.imgPreview)
        btnSimpan = findViewById(R.id.btn_simpan)

        // Cek apakah intent membawa data untuk edit
        val product = intent.getSerializableExtra("product") as? Product
        if (product != null) {
            isEditMode = true
            productId = product.id
            etNama.setText(product.name)
            etHarga.setText(product.price.toString())
            etDeskripsi.setText(product.description)
            etStok.setText(product.stock.toString())
            imageUrl = product.imageUrl
            Glide.with(this).load(imageUrl).into(imgPreview)
        } else {
            // mode tambah
            productId = firestore.collection("products").document().id
        }

        btnTambahGambar.setOnClickListener {
            val input = EditText(this)
            input.hint = "https://..."
            AlertDialog.Builder(this)
                .setTitle("Masukkan URL Gambar")
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val url = input.text.toString()
                    if (url.isNotEmpty()) {
                        imageUrl = url
                        Glide.with(this).load(url).into(imgPreview)
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        btnSimpan.setOnClickListener {
            simpanProduk()
        }
    }

    private fun simpanProduk() {
        val nama = etNama.text.toString().trim()
        val harga = etHarga.text.toString().toIntOrNull() ?: 0
        val deskripsi = etDeskripsi.text.toString().trim()
        val stok = etStok.text.toString().toIntOrNull() ?: 0
        val sellerId = auth.currentUser?.uid ?: return

        if (nama.isEmpty() || harga <= 0 || imageUrl.isEmpty()) {
            Toast.makeText(this, "Nama, harga, dan gambar wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val product = Product(
            id = productId,
            name = nama,
            price = harga,
            description = deskripsi,
            imageUrl = imageUrl,
            stock = stok,
            sellerId = sellerId,
            sold = 0,
            views = 0
        )

        firestore.collection("products").document(productId)
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    if (isEditMode) "Produk berhasil diperbarui" else "Produk berhasil ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // kembali ke SellerProductActivity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan produk", Toast.LENGTH_SHORT).show()
            }
    }
}
