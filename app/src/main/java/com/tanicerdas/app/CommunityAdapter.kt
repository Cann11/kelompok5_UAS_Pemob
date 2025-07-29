package com.tanicerdas.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CommunityAdapter(private val data: List<Community>) :
    RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama = view.findViewById<TextView>(R.id.tvGroupName)
        val deskripsi = view.findViewById<TextView>(R.id.tvGroupDesc)
        val foto = view.findViewById<ImageView>(R.id.imgGroupPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val komunitas = data[position]
        holder.nama.text = komunitas.nama
        holder.deskripsi.text = komunitas.deskripsi

        if (komunitas.foto.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(komunitas.foto)
                .circleCrop()
                .into(holder.foto)
        }
    }

    override fun getItemCount(): Int = data.size
}

