package com.example.tohwangsoi_mobile.model

data class OrderItem(
    val userName: String,
    val packageName: String,
    val price: String,
    val date: String,
    var isApproved: Boolean = false
)
