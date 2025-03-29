package com.example.tohwangsoi_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.Myorder

// Interface สำหรับการคลิกปุ่มใน Adapter
interface OnOrderActionListener {
    fun onCancelOrder(position: Int)
    fun onPayOrder(position: Int)
}

class MyorderAdapter(
    private val orders: ArrayList<Myorder>,
    private val listener: OnOrderActionListener // รับ listener สำหรับจัดการการคลิก
) : RecyclerView.Adapter<MyorderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val tvPackage = itemView.findViewById<TextView>(R.id.tvPackage)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val province = itemView.findViewById<TextView>(R.id.province)
        val place = itemView.findViewById<TextView>(R.id.place)
        val guest = itemView.findViewById<TextView>(R.id.guest)
        val btnCancle : Button = itemView.findViewById(R.id.btnCancle)
        val btnPay : Button = itemView.findViewById(R.id.btnPay) // ปุ่มจ่าย
        val tvApprovalStatus : TextView = itemView.findViewById(R.id.tvApprovalStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_myorder, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.tvUserName.text = "รายการออเดอร์ที่ : ${order.orderNo}"
        holder.tvPackage.text = "แพ็กเกจที่สั่งจัด : ${order.packageName}"
        holder.tvPrice.text = "ราคา: ${order.price}"
        holder.tvDate.text = "วันที่จอง: ${order.date}"
        holder.province.text = "จังหวัด: ${order.province}"
        holder.place.text = "สถานที่จัดงาน: ${order.place}"
        holder.guest.text = "จำนวนแขก: ${order.guest}"

        // แสดงสถานะ "รออนุมัติ" หรือ ปุ่ม "ยกเลิก" / "จ่ายเงิน"
        if (order.isApproved) {
            holder.tvApprovalStatus.text = "อนุมัติเเล้ว" // ไม่มีข้อความ "รออนุมัติ" ถ้าอนุมัติแล้ว
            holder.tvApprovalStatus.visibility = View.VISIBLE // ซ่อนข้อความเมื่ออนุมัติ
            holder.btnCancle.visibility = View.GONE // ซ่อนปุ่มยกเลิก
            holder.btnPay.visibility = View.VISIBLE // แสดงปุ่ม "จ่าย"
            holder.btnPay.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green)) // สีเขียว

            holder.btnPay.text = "จ่ายเงิน" // เปลี่ยนข้อความของปุ่ม
        } else {
            holder.tvApprovalStatus.text = "รออนุมัติ" // ข้อความ "รออนุมัติ" ถ้ายังไม่ได้อนุมัติ
            holder.tvApprovalStatus.visibility = View.VISIBLE
            holder.btnCancle.visibility = View.VISIBLE
            holder.btnPay.visibility = View.GONE // ซ่อนปุ่มจ่ายเงิน
        }

        if (order.isCancell) {
            // ถ้ายกเลิกแล้ว
            holder.btnCancle.text = "ยกเลิกแล้ว"
            holder.btnCancle.setEnabled(false) // ปิดใช้งานปุ่ม "ยกเลิก"
        } else {
            // ถ้ายังไม่ได้ยกเลิก
            holder.btnCancle.text = "❌ยกเลิกออเดอร์"
            holder.btnCancle.setEnabled(true) // เปิดใช้งานปุ่ม "ยกเลิก"
        }

        // การกดปุ่ม "ยกเลิก"
        holder.btnCancle.setOnClickListener {
            order.isCancell = true // เปลี่ยนสถานะการยกเลิก
            listener.onCancelOrder(position) // แจ้งให้ Activity/Fragment รับรู้เมื่อกดปุ่ม "ยกเลิก"
            notifyItemChanged(position) // อัปเดต RecyclerView
        }

        // การกดปุ่ม "จ่ายเงิน"
        holder.btnPay.setOnClickListener {
            listener.onPayOrder(position) // แจ้งให้ Activity/Fragment รับรู้เมื่อกดปุ่ม "จ่ายเงิน"
        }
    }

    override fun getItemCount(): Int = orders.size
}
