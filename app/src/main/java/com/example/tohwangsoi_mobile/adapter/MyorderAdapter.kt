package com.example.tohwangsoi_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.Myorder

class MyorderAdapter(private val orders: ArrayList<Myorder>) :
    RecyclerView.Adapter<MyorderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val tvPackage = itemView.findViewById<TextView>(R.id.tvPackage)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val province = itemView.findViewById<TextView>(R.id.province)
        val place = itemView.findViewById<TextView>(R.id.place)
        val guest = itemView.findViewById<TextView>(R.id.guest)
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
    }

    override fun getItemCount(): Int = orders.size
}
