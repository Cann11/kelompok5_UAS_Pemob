package com.tanicerdas.app

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class EducationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EducationAdapter
    private val listEdukasi = mutableListOf<Education>()
    private lateinit var progressBar: ProgressBar
    private lateinit var searchEditText: EditText
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_education, container, false)

        recyclerView = view.findViewById(R.id.rv_edukasi)
        progressBar = view.findViewById(R.id.progressBar)
        searchEditText = view.findViewById(R.id.et_search)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EducationAdapter(listEdukasi) { edukasi ->
            // Navigasi ke DetailEdukasiFragment
            val detailFragment = DetailEdukasiFragment.newInstance(edukasi)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter

        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString().trim()
            ambilDataEdukasi(query)
            true
        }

        ambilDataEdukasi()

        return view
    }

    private fun ambilDataEdukasi(query: String = "") {
        progressBar.visibility = View.VISIBLE

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("edukasi").orderBy("judul")

        // Stop listener sebelumnya jika ada
        listenerRegistration?.remove()

        listenerRegistration = ref.addSnapshotListener { snapshot, error ->
            progressBar.visibility = View.GONE
            if (error != null || snapshot == null) return@addSnapshotListener

            listEdukasi.clear()
            for (doc in snapshot.documents) {
                val edukasi = doc.toObject(Education::class.java)
                if (edukasi != null && (query.isEmpty() || edukasi.judul.contains(query, ignoreCase = true))) {
                    listEdukasi.add(edukasi)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hentikan listener Firestore saat fragment dihancurkan
        listenerRegistration?.remove()
    }
}
