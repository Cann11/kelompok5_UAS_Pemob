package com.tanicerdas.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class KonsultasiAdapter(private val listAhli: List<AhliPertanianModel>) :
    RecyclerView.Adapter<KonsultasiAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoto: ImageView = itemView.findViewById(R.id.iv_foto_ahli)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_ahli)
        val tvBidang: TextView = itemView.findViewById(R.id.tv_bidang)
        val tvPengalaman: TextView = itemView.findViewById(R.id.tv_pengalaman)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ahli_konsultasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ahli = listAhli[position]

        holder.tvNama.text = ahli.nama
        holder.tvBidang.text = ahli.bidang
        holder.tvPengalaman.text = ahli.pengalaman

        Glide.with(holder.itemView.context)
            .load(ahli.fotoProfil) // ini harus berupa URL dari Firestore
            .placeholder(R.drawable.ic_profil) // default kalau foto belum selesai dimuat
            .circleCrop()
            .into(holder.ivFoto)
    }

    override fun getItemCount(): Int = listAhli.size
}
