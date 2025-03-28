package com.example.tohwangsoi_mobile.model

data class OrderItem(
    val orderNo: String,
    val userName: String,
    val packageName: String,
    val province: String,
    val price: String,
    val place: String,
    val date: String,
    val guest: String,
    var isApproved: Boolean = false
) {



}
