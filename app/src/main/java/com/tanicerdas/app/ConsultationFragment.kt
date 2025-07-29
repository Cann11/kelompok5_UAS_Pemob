package com.tanicerdas.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ConsultationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var konsultasiAdapter: KonsultasiAdapter
    private val ahliList = mutableListOf<AhliPertanianModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)

        recyclerView = view.findViewById(R.id.rv_ahli_pertanian)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        konsultasiAdapter = KonsultasiAdapter(ahliList)
        recyclerView.adapter = konsultasiAdapter

        ambilDataDariFirestore()

        return view
    }

    private fun ambilDataDariFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("konsultan")
            .get()
            .addOnSuccessListener { result ->
                ahliList.clear()
                for (document in result) {
                    val ahli = document.toObject(AhliPertanianModel::class.java)
                    ahliList.add(ahli)
                }
                konsultasiAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Gagal ambil data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
