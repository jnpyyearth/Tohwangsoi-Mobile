package com.example.tohwangsoi_mobile.mode

import com.example.tohwangsoi_mobile.model.OrderItem
object MockData {
    val orders = listOf(
            OrderItem("1", "สมชาย", "แพ็กเกจ A", "กรุงเทพ", "3,500", "อิมแพค เมืองทองธานี", "21 มีนาคม 2025", "100 คน"),
            OrderItem("2", "มาลี", "แพ็กเกจ B", "ชลบุรี", "5,000", "ม.เกษตร ศรีราชา", "22 มีนาคม 2025", "80 คน"),
            OrderItem("3", "อนันต์", "แพ็กเกจ C", "เชียงใหม่", "7,200", "ศูนย์ประชุม", "23 มีนาคม 2025", "120 คน"),
            OrderItem("4", "สมศักดิ์", "แพ็กเกจ C", "นครปฐม", "7,200", "ลานวัดใหญ่", "23 มีนาคม 2025", "110 คน")
    )
}
