package com.tanicerdas.app


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class KalenderFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var tvMusimInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kalender, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        tvMusimInfo = view.findViewById(R.id.tvMusimInfo)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                .format(selectedDate.time)

            // Nanti ini diganti pakai AI musim
            tvMusimInfo.text = "Info musim untuk tanggal $formattedDate:\nMusim Hujan - Cocok tanam padi"
        }

        return view
    }
}