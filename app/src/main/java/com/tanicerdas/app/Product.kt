package com.tanicerdas.app

import java.io.Serializable

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    val stock: Int = 0,
    val sold: Int = 0,
    val views: Int = 0,
    val sellerId: String = "",
    val promoted: Boolean = false
) : Serializable

