package com.example.tohwangsoi_mobile.model

data class MenuManagerItem(
    var id: String = "",
    var MenuName:String ="",
    val Category:String ="",
    val Image:String ="",
    val isHeader: Boolean = false
)