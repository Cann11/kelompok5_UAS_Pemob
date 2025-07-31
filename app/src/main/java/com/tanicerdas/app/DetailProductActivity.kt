package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DetailProductActivity : AppCompatActivity() {

    private lateinit var imgProduct: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDesc: TextView
    private lateinit var btnCart: Button
    private lateinit var btnBuy: Button

    private lateinit var rvRelated: RecyclerView
    private lateinit var relatedAdapter: ProductAdapter
    private val relatedList = mutableListOf<Product>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        // Inisialisasi view
        imgProduct = findViewById(R.id.img_detail_product)
        tvName = findViewById(R.id.tv_detail_name)
        tvPrice = findViewById(R.id.tv_detail_price)
        tvDesc = findViewById(R.id.tv_detail_desc)
        btnCart = findViewById(R.id.btn_add_to_cart)
        btnBuy = findViewById(R.id.btn_buy_now)
        rvRelated = findViewById(R.id.rv_related_products)

        // Ambil data produk dari intent
        product = intent.getSerializableExtra("PRODUCT") as Product

        // Set data ke tampilan
        tvName.text = product.name
        tvPrice.text = "Rp${product.price}"
        tvDesc.text = product.description
        Glide.with(this).load(product.imageUrl).into(imgProduct)

        // Tombol tambah ke keranjang
        btnCart.setOnClickListener {
            addToCart()
        }

        // Tombol beli langsung
        btnBuy.setOnClickListener {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Setup RecyclerView produk lainnya
        rvRelated.layoutManager = GridLayoutManager(this, 2)
        relatedAdapter = ProductAdapter(relatedList) { selectedProduct ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra("PRODUCT", selectedProduct)
            startActivity(intent)
        }
        rvRelated.adapter = relatedAdapter

        loadRelatedProducts()
    }

    private fun addToCart() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItemRef = db.collection("cart")
            .document(userId)
            .collection("items")
            .document(product.id)

        cartItemRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val currentQty = document.getLong("quantity") ?: 1
                cartItemRef.update("quantity", currentQty + 1)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Jumlah produk ditambah ke keranjang", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val cartItem = hashMapOf(
                    "productId" to product.id,
                    "name" to product.name,
                    "price" to product.price,
                    "imageUrl" to product.imageUrl,
                    "quantity" to 1
                )
                cartItemRef.set(cartItem, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRelatedProducts() {
        db.collection("products")
            .whereNotEqualTo("id", product.id)
            .limit(4)
            .get()
            .addOnSuccessListener { result ->
                relatedList.clear()
                for (doc in result) {
                    val item = doc.toObject(Product::class.java)
                    relatedList.add(item)
                }
                relatedAdapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal memuat produk lainnya", Toast.LENGTH_SHORT).show()
            }
    }
}
