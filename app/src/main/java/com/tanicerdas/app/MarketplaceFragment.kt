package com.tanicerdas.app

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarketplaceFragment : Fragment() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marketplace, container, false)

        searchField = view.findViewById(R.id.et_search_market)
        productRecyclerView = view.findViewById(R.id.rv_products)

        // Dummy data
        val products = listOf(
            Product("Pupuk Organik", "Rp 25.000", R.drawable.ic_pupuk1),
            Product("Benih Cabai", "Rp 15.000", R.drawable.ic_benih),
            Product("Alat Semprot", "Rp 75.000", R.drawable.ic_alat),
            Product("Pestisida Nabati", "Rp 40.000", R.drawable.ic_pestisida),
            Product("Tanah Subur", "Rp 20.000", R.drawable.ic_nyebor),
            Product("Em4 Mikroba", "Rp 18.000", R.drawable.ic_farmer)
        )

        val adapter = ProductAdapter(products)
        productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productRecyclerView.adapter = adapter

        searchField.setOnEditorActionListener { _, _, _ ->
            Toast.makeText(requireContext(), "Cari produk: ${searchField.text}", Toast.LENGTH_SHORT).show()
            true
        }

        return view
    }
}
