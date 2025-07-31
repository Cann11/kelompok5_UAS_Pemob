package com.tanicerdas.app

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val menuUbahPassword = view.findViewById<LinearLayout>(R.id.itemPassword)

        menuUbahPassword.setOnClickListener {
            showChangePasswordDialog()
        }

        return view
    }

    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_ubah_password, null)

        val currentPass = dialogView.findViewById<EditText>(R.id.etCurrentPassword)
        val newPass = dialogView.findViewById<EditText>(R.id.etNewPassword)
        val confirmPass = dialogView.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSimpan = dialogView.findViewById<Button>(R.id.btnSave)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnCancel)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnBatal.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSimpan.setOnClickListener {
            val current = currentPass.text.toString()
            val newPassword = newPass.text.toString()
            val confirmPassword = confirmPass.text.toString()

            if (current.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Password baru tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            val email = user?.email

            if (email != null) {
                val credential = EmailAuthProvider.getCredential(email, current)
                user.reauthenticate(credential).addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            db.collection("users").document(user.uid)
                                .update("lastPasswordChange", com.google.firebase.firestore.FieldValue.serverTimestamp())

                            Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal mengubah password", Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Password lama salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alertDialog.show()
    }

}
