package com.tanicerdas.app

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailEdukasiFragment : Fragment() {

    companion object {
        const val ARG_JUDUL = "judul"
        const val ARG_KATEGORI = "kategori"
        const val ARG_GAMBAR = "gambar"
        const val ARG_KONTEN = "konten"

        fun newInstance(edukasi: Education): DetailEdukasiFragment {
            val fragment = DetailEdukasiFragment()
            val bundle = Bundle().apply {
                putString(ARG_JUDUL, edukasi.judul)
                putString(ARG_KATEGORI, edukasi.kategori)
                putString(ARG_GAMBAR, edukasi.gambar)
                putString(ARG_KONTEN, edukasi.konten)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_edukasi, container, false)

        val imgEdukasi = view.findViewById<ImageView>(R.id.imgEdukasi)
        val tvJudul = view.findViewById<TextView>(R.id.tvJudul)
        val tvKategori = view.findViewById<TextView>(R.id.tvKategori)
        val tvIsi = view.findViewById<TextView>(R.id.tvIsi)

        arguments?.let {
            tvJudul.text = it.getString(ARG_JUDUL)
            tvKategori.text = it.getString(ARG_KATEGORI)
            tvIsi.text = it.getString(ARG_KONTEN)

            Glide.with(requireContext())
                .load(it.getString(ARG_GAMBAR))
                .placeholder(R.drawable.ic_menu_gallery)
                .into(imgEdukasi)
        }

        return view
    }
}
