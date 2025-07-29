package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnEditProfil: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        tvNama = view.findViewById(R.id.tv_nama_pengguna)
        tvEmail = view.findViewById(R.id.tv_email_pengguna)
        btnEditProfil = view.findViewById(R.id.btn_edit_profil)

        val menuPesanan = view.findViewById<LinearLayout>(R.id.menu_pesanan)
        val menuFavorit = view.findViewById<LinearLayout>(R.id.menu_favorit)
        val menuEdukasi = view.findViewById<LinearLayout>(R.id.menu_riwayat_edukasi)
        val menuKonsultasi = view.findViewById<LinearLayout>(R.id.menu_jadwal_konsultasi)
        val menuLogout = view.findViewById<LinearLayout>(R.id.menu_logout)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            tvNama.text = currentUser.displayName ?: "Nama Pengguna"
            tvEmail.text = currentUser.email ?: "email@example.com"
        } else {
            tvNama.text = "Tidak Terautentikasi"
            tvEmail.text = "-"
        }

        btnEditProfil.setOnClickListener {
            Toast.makeText(requireContext(), "Edit profil diklik", Toast.LENGTH_SHORT).show()
            // Navigasi ke EditProfileActivity (kalau ada)
        }

        menuPesanan.setOnClickListener {
            Toast.makeText(requireContext(), "Pesanan Saya diklik", Toast.LENGTH_SHORT).show()
        }

        menuFavorit.setOnClickListener {
            Toast.makeText(requireContext(), "Favorit Saya diklik", Toast.LENGTH_SHORT).show()
        }

        menuEdukasi.setOnClickListener {
            Toast.makeText(requireContext(), "Riwayat Edukasi diklik", Toast.LENGTH_SHORT).show()
        }

        menuKonsultasi.setOnClickListener {
            Toast.makeText(requireContext(), "Jadwal Konsultasi diklik", Toast.LENGTH_SHORT).show()
        }

        menuLogout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            // Navigasi ke LoginActivity (kalau perlu)
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
