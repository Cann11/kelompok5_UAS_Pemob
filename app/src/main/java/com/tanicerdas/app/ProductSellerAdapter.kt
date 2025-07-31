package com.tanicerdas.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductSellerAdapter(
    private val productList: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit,
    private val onPromoteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductSellerAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtPromo: TextView = itemView.findViewById(R.id.txtPromo)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtStok: TextView = itemView.findViewById(R.id.txtStok)
        val txtTerjual: TextView = itemView.findViewById(R.id.txtTerjual)
        val txtDilihat: TextView = itemView.findViewById(R.id.txtDilihat)
        val btnUbah: Button = itemView.findViewById(R.id.btnUbah)
        val btnArsipkan: Button = itemView.findViewById(R.id.btnArsipkan)
        val btnPromosi: Button = itemView.findViewById(R.id.btnIklankan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_seller, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.txtProductName.text = product.name
        holder.txtPrice.text = "Rp${product.price}"
        holder.txtStok.text = "Stok ${product.stock}"
        holder.txtTerjual.text = "Terjual ${product.sold}"
        holder.txtDilihat.text = "Dilihat ${product.views}"

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_menu_gallery)
            .into(holder.imgProduct)

        holder.btnUbah.setOnClickListener { onEditClick(product) }
        holder.btnArsipkan.setOnClickListener { onDeleteClick(product) }
        holder.btnPromosi.setOnClickListener { onPromoteClick(product) }
    }
}
