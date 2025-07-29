package com.tanicerdas.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tanicerdas.app.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Simpan view hasil inflate
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi tombol
        val btnKalender = view.findViewById<LinearLayout>(R.id.btnKalender)
        val btnKomunitas = view.findViewById<LinearLayout>(R.id.btnKomunitas)
        val btnEdukasi = view.findViewById<LinearLayout>(R.id.btnEdukasi)
        val btnPasar = view.findViewById<LinearLayout>(R.id.btnPasar)
        btnKalender.setOnClickListener {
            // Navigasi ke KalenderFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, KalenderFragment())
                .addToBackStack(null)
                .commit()
        }

        btnKomunitas.setOnClickListener {
            // Navigasi ke CommunityFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CommunityFragment())
                .addToBackStack(null)
                .commit()
        }

        btnEdukasi.setOnClickListener {
            // Navigasi ke CommunityFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EducationFragment())
                .addToBackStack(null)
                .commit()
        }

        btnPasar.setOnClickListener {
            // Navigasi ke CommunityFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MarketplaceFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
