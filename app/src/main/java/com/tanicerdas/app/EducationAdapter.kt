package com.tanicerdas.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EducationAdapter(
    private val list: List<Education>,
    private val onClick: (Education) -> Unit
) : RecyclerView.Adapter<EducationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEdukasi: ImageView = itemView.findViewById(R.id.imgEdukasi)
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        val tvKategori: TextView = itemView.findViewById(R.id.tvKategori)

        fun bind(edukasi: Education) {
            tvJudul.text = edukasi.judul
            tvKategori.text = edukasi.kategori

            Glide.with(itemView.context)
                .load(edukasi.gambar)
                .placeholder(R.drawable.ic_menu_gallery) // ganti sesuai gambar placeholder kamu
                .into(imgEdukasi)

            itemView.setOnClickListener { onClick(edukasi) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edukasi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
