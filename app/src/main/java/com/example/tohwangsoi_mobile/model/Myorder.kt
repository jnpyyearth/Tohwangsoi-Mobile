package com.example.tohwangsoi_mobile.model

data class Myorder(
    val orderNo: String,          // หมายเลขออเดอร์
    val packageName: String,      // ชื่อแพ็กเกจ
    val price: String,            // ราคา
    val date: String,             // วันที่จอง
    val province: String,         // จังหวัด
    val place: String,            // สถานที่จัดงาน
    val guest: String,            // จำนวนแขก
    var isCancell: Boolean = false, // สถานะยกเลิก
    var isApproved: Boolean = false // สถานะอนุมัติ
)
