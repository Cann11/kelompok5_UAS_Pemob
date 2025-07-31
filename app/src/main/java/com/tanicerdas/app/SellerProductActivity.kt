package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SellerProductActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var btnAddProduct: Button
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_product)

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)
        btnAddProduct = findViewById(R.id.btnAddProduct)

        recyclerViewProducts.layoutManager = LinearLayoutManager(this)

        btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        val currentUser = auth.currentUser ?: return
        firestore.collection("products")
            .whereEqualTo("sellerId", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                val productList = result.mapNotNull { it.toObject(Product::class.java) }

                val adapter = ProductSellerAdapter(
                    productList,
                    onEditClick = { product -> editProduct(product) },
                    onDeleteClick = { product -> deleteProduct(product) },
                    onPromoteClick = { product -> promoteProduct(product) }
                )
                recyclerViewProducts.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat produk", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editProduct(product: Product) {
        val intent = Intent(this, AddProductActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    private fun deleteProduct(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Yakin ingin menghapus produk \"${product.name}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                firestore.collection("products").document(product.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Produk dihapus", Toast.LENGTH_SHORT).show()
                        loadProducts()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun promoteProduct(product: Product) {
        Toast.makeText(this, "Fitur Promosi Comingsoon", Toast.LENGTH_SHORT).show()
    }
}
