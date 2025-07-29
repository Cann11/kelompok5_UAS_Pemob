package com.tanicerdas.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

data class Product(val name: String, val price: String, val imageRes: Int)

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.img_product)
        val tvName: TextView = view.findViewById(R.id.tv_product_name)
        val tvPrice: TextView = view.findViewById(R.id.tv_product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = productList[position]
        holder.imgProduct.setImageResource(p.imageRes)
        holder.tvName.text = p.name
        holder.tvPrice.text = p.price
    }

    override fun getItemCount(): Int = productList.size
}
