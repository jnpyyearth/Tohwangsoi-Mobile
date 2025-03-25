package com.example.tohwangsoi_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.OrderItem

class OrderAdapter(private val orderList: List<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvPackage: TextView = view.findViewById(R.id.tvPackage)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.tvUserName.text = "ผู้จอง: ${order.userName}"
        holder.tvPackage.text = "แพ็กเกจ: ${order.packageName}"
        holder.tvPrice.text = "ราคา: ${order.price} บาท"
        holder.tvDate.text = "วันที่จอง: ${order.date}"

        if (order.isApproved) {
            holder.btnApprove.text = "อนุมัติแล้ว"
            holder.btnApprove.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))

        } else {
            holder.btnApprove.text = "อนุมัติการจอง"
            holder.btnApprove.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.purple_500))

        }
        holder.btnApprove.setOnClickListener {
            val selectDate =order.date
            if(!order.isApproved){
                val isSameDateApproved = orderList.any {
                    it.date == selectDate && it.isApproved && it != order
                }
                if (isSameDateApproved) {
                    Toast.makeText(
                        holder.itemView.context,
                        "ไม่สามารถอนุมัติรายการซ้ำในวันเดียวกันได้",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

            }
            // สลับสถานะ
            orderList[position].isApproved = !orderList[position].isApproved
            notifyItemChanged(position) // รีเฟรช item ที่ตำแหน่งนั้น
        }
    }

    override fun getItemCount(): Int = orderList.size
}
