package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MarketplaceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marketplace, container, false)
        recyclerView = view.findViewById(R.id.rv_products)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("PRODUCT", product)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        loadProductsFromFirebase()
        return view
    }

    private fun loadProductsFromFirebase() {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()
                for (doc in result) {
                    val product = doc.toObject(Product::class.java)
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
    }
}
