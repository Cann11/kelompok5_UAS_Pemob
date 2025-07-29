package com.tanicerdas.app

import com.google.firebase.Timestamp

data class Community(
    val id: String = "",
    val nama: String = "",
    val deskripsi: String = "",
    val kategori: String = "",
    val foto: String = "",
    val dibuat_oleh: String = "",
    val tanggal_dibuat: com.google.firebase.Timestamp? = null,
    val anggota: List<String> = emptyList(),
    val status: String = ""
)



