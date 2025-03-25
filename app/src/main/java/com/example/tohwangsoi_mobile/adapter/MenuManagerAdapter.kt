package com.example.tohwangsoi_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.MenuManagerItem

class MenuManagerAdapter(private val menuList: List<MenuManagerItem>) :
    RecyclerView.Adapter<MenuManagerAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMenu: ImageView = view.findViewById(R.id.imgMenu)
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menumanger, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]

        holder.tvMenuName.text = item.MenuName // ✅ แก้ชื่อ Field ให้ถูกต้อง
//        holder.tvCategory.text = "ประเภท: ${item.Category}"

        // ✅ แปลง Google Drive URL เป็น Direct Link ถ้าจำเป็น
        val imageUrl = convertGoogleDriveUrl(item.Image)

        // โหลดรูปจาก URL ด้วย Glide
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground) // รูปแทนขณะโหลด
             // รูปแสดงเมื่อโหลดล้มเหลว
            .into(holder.imgMenu)
    }

    override fun getItemCount(): Int = menuList.size

    // ✅ ฟังก์ชันแปลง Google Drive URL เป็น Direct Link
    private fun convertGoogleDriveUrl(url: String): String {
        return if (url.contains("drive.google.com")) {
            val fileId = url.split("/d/").getOrNull(1)?.split("/")?.getOrNull(0)
            "https://drive.google.com/uc?export=view&id=$fileId"
        } else {
            url // ถ้าเป็น URL ปกติให้ใช้ตามเดิม
        }
    }
}
