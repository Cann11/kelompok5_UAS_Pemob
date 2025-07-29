package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*

class CommunityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityAdapter
    private val list = mutableListOf<Community>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        recyclerView = view.findViewById(R.id.rvCommunities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CommunityAdapter(list)
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.fabAddCommunity).setOnClickListener {
            startActivity(Intent(requireContext(), CreateCommunityActivity::class.java))
        }

        ambilDataKomunitas()

        return view
    }

    private fun ambilDataKomunitas() {
        FirebaseFirestore.getInstance().collection("komunitas")
            .whereEqualTo("status", "Aktif")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                list.clear()
                for (doc in snapshot!!) {
                    val komunitas = doc.toObject(Community::class.java)
                    list.add(komunitas)
                }
                adapter.notifyDataSetChanged()
            }
    }
}

