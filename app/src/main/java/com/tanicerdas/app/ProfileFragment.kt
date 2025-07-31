package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivFoto: ImageView
    private lateinit var btnEditProfil: Button
    private lateinit var tvMenuPenjual: TextView
    private lateinit var menuPenjual: LinearLayout

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var rolePengguna: String = "user" // menyimpan role saat ini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi View
        tvNama = view.findViewById(R.id.tv_nama_pengguna)
        tvEmail = view.findViewById(R.id.tv_email_pengguna)
        ivFoto = view.findViewById(R.id.iv_profile_picture)
        btnEditProfil = view.findViewById(R.id.btn_edit_profil)
        menuPenjual = view.findViewById(R.id.menu_daftar_penjual)
        tvMenuPenjual = view.findViewById(R.id.tv_menu_penjual)

        // Tombol edit profil
        btnEditProfil.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Listener tombol penjual (dipasang langsung dan tidak diubah lagi)
        menuPenjual.setOnClickListener {
            if (rolePengguna == "seller") {
                startActivity(Intent(requireContext(), SellerDashboardActivity::class.java))
            } else {
                startActivity(Intent(requireContext(), RegisterSellerActivity::class.java))
            }
        }

        // Menu lainnya
        view.findViewById<LinearLayout>(R.id.menu_pesanan).setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Pesanan Saya Comingsoon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<LinearLayout>(R.id.menu_favorit).setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Favorit Saya  Comingsoon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<LinearLayout>(R.id.menu_riwayat_edukasi).setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Riwayat Edukasi Comingsoon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<LinearLayout>(R.id.menu_jadwal_konsultasi).setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Jadwal Konsultasi Comingsoon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<LinearLayout>(R.id.menu_pengaturan).setOnClickListener {
            Toast.makeText(requireContext(), "Pengaturan diklick", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .addToBackStack(null) // agar bisa kembali
                .commit()
        }
        view.findViewById<LinearLayout>(R.id.menu_logout).setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            tvEmail.text = currentUser.email ?: "-"
            tampilkanDataUser(currentUser)
        }
    }

    private fun tampilkanDataUser(user: FirebaseUser) {
        val uid = user.uid
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val nama = doc.getString("name") ?: "Pengguna"
                    val fotoUrl = doc.getString("photoUrl")
                    val role = doc.getString("role") ?: "user"

                    rolePengguna = role // simpan role ke variabel global

                    tvNama.text = nama
                    tvMenuPenjual.text = if (role == "seller") "Toko Saya" else "Daftar Sebagai Penjual"

                    Glide.with(requireContext())
                        .load(fotoUrl)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .circleCrop()
                        .into(ivFoto)
                } else {
                    tampilkanDefaultUser()
                }
            }
            .addOnFailureListener {
                tampilkanDefaultUser()
            }
    }

    private fun tampilkanDefaultUser() {
        rolePengguna = "user"
        tvNama.text = "Data tidak ditemukan"
        tvMenuPenjual.text = "Daftar Sebagai Penjual"

        Glide.with(requireContext())
            .load(R.drawable.ic_user)
            .circleCrop()
            .into(ivFoto)
    }
}
