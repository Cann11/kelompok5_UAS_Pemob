package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SellerDashboardActivity : AppCompatActivity() {

    private lateinit var txtSellerName: TextView
    private lateinit var txtPerluDikirim: TextView
    private lateinit var txtPembatalan: TextView
    private lateinit var txtRetur: TextView
    private lateinit var txtReview: TextView
    private lateinit var imgStore: ImageView

    private lateinit var produkLayout: LinearLayout
    private lateinit var keuanganLayout: LinearLayout
    private lateinit var performaLayout: LinearLayout
    private lateinit var chatLayout: LinearLayout
    private lateinit var promosiLayout: LinearLayout
    private lateinit var bantuanLayout: LinearLayout

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)

        txtSellerName = findViewById(R.id.txtSellerName)
        txtPerluDikirim = findViewById(R.id.txtPerluDikirim)
        txtPembatalan = findViewById(R.id.txtPembatalan)
        txtRetur = findViewById(R.id.txtRetur)
        txtReview = findViewById(R.id.txtReview)
        imgStore = findViewById(R.id.imgStore)

        produkLayout = findViewById(R.id.menu_produk)
        keuanganLayout = findViewById(R.id.menu_keuangan)
        performaLayout = findViewById(R.id.menu_performa)
        chatLayout = findViewById(R.id.menu_chat)
        promosiLayout = findViewById(R.id.menu_promosi)
        bantuanLayout = findViewById(R.id.menu_bantuan)

        loadStoreInfo()
        loadStoreStatus()

        produkLayout.setOnClickListener { showToast("Buka Produk")
            val intent = Intent(this, SellerProductActivity::class.java)
            startActivity(intent)
        }
        keuanganLayout.setOnClickListener { showToast("Fitur Keuangan Coming Soon") }
        performaLayout.setOnClickListener { showToast("Fitur Performa Coming Soon") }
        chatLayout.setOnClickListener { showToast("Fitur Chat Coming Soon") }
        promosiLayout.setOnClickListener { showToast("Fitur Promosi Coming Soon") }
        bantuanLayout.setOnClickListener { showToast("Fitur Bantuan Coming Soon") }
    }

    private fun loadStoreInfo() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            txtSellerName.text = "Toko TaniCerdas"
            return
        }

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val storeName = document.getString("storeName") ?: "Toko TaniCerdas"
                val photoUrl = document.getString("photoUrl")

                txtSellerName.text = storeName
                imgStore.loadCircleImage(photoUrl)
            }
            .addOnFailureListener {
                txtSellerName.text = "Toko TaniCerdas"
                showToast("Gagal memuat info toko")
            }
    }

    private fun loadStoreStatus() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("storeStatus").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    txtPerluDikirim.text = (document.getLong("perluDikirim") ?: 0).toString()
                    txtPembatalan.text = (document.getLong("pembatalan") ?: 0).toString()
                    txtRetur.text = (document.getLong("retur") ?: 0).toString()
                    txtReview.text = (document.getLong("review") ?: 0).toString()
                } else {
                    showToast("Status toko tidak ditemukan")
                }
            }
            .addOnFailureListener {
                showToast("Gagal memuat status toko")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun ImageView.loadCircleImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_menu_gallery) // Ganti placeholder sesuai drawable kamu
                    .error(R.drawable.ic_menu_gallery)
            )
            .into(this)
    }
}
