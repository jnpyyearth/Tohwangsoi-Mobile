package com.example.tohwangsoi_mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.MenuManagerItem

class SectionedMenuSelectAdapter(
    private val menuList: List<MenuManagerItem>, // รายการเมนูทั้งหมด ทั้ง header และ item
    private val onSelectionChanged: (List<MenuManagerItem>) -> Unit // callback ส่งรายการที่เลือกกลับไป
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0 // ประเภท header
    private val TYPE_ITEM = 1   // ประเภทเมนูทั่วไป
    private val selectedItems = mutableListOf<MenuManagerItem>() // รายการเมนูที่ถูกเลือก

    // ViewHolder สำหรับ header (หมวดหมู่)
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategoryHeader)
    }

    // ViewHolder สำหรับเมนูที่เลือกได้
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val imgMenu: ImageView = view.findViewById(R.id.imgMenu)
        val checkbox: CheckBox = view.findViewById(R.id.checkboxSelect)
    }

    // ระบุประเภท view ในตำแหน่งนั้น ๆ
    override fun getItemViewType(position: Int): Int {
        return if (menuList[position].isHeader) TYPE_HEADER else TYPE_ITEM
    }

    // สร้าง ViewHolder ให้เหมาะกับประเภทที่ระบุ
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_checkbox, parent, false)
            ItemViewHolder(view)
        }
    }

    // กำหนดค่าข้อมูลให้กับ ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = menuList[position]

        if (holder is HeaderViewHolder) {
            holder.tvCategory.text = item.MenuName // แสดงชื่อหมวดหมู่

        } else if (holder is ItemViewHolder) {
            holder.tvMenuName.text = item.MenuName // แสดงชื่อเมนู

            val imageUrl = convertGoogleDriveUrl(item.Image) // แปลง URL ก่อนโหลดภาพ

            // โหลดรูปภาพเมนูจาก URL
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgMenu)

            // รีเซ็ต listener ก่อนตั้งค่าใหม่ (กัน onCheckedChangeListener loop)
            holder.checkbox.setOnCheckedChangeListener(null)

            // ติ๊ก checkbox ตามรายการที่เลือกไว้ก่อนหน้า
            holder.checkbox.isChecked = selectedItems.contains(item)
            holder.checkbox.isEnabled = canSelectItem(item.Category)
            // ถ้ามีการเปลี่ยนแปลง checkbox
            holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (selectedItems.filter { it.Category == item.Category }.size >= 2) {
                        // ถ้ามีการเลือกเกิน 2 รายการในหมวดนี้
                        holder.checkbox.isChecked = false  // ยกเลิกการเลือก
                        Toast.makeText(holder.itemView.context, "ไม่สามารถเลือกได้เกิน 2 เมนูในหมวดนี้", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedItems.add(item) // เพิ่มใน list
                    }
                } else {
                    selectedItems.remove(item) // ลบออก
                }
                onSelectionChanged(selectedItems) // แจ้ง parent ว่ารายการเลือกเปลี่ยนแล้ว
            }
        }
    }

    // จำนวน item ทั้งหมดใน list
    override fun getItemCount(): Int = menuList.size

    // คืนค่ารายการเมนูที่ถูกเลือกทั้งหมด
    fun getSelectedItems(): List<MenuManagerItem> = selectedItems

    // แปลง Google Drive URL ให้ใช้กับ Glide ได้
    private fun convertGoogleDriveUrl(originalUrl: String): String {
        val regex = Regex("https://drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)/view\\?usp=sharing")
        val match = regex.find(originalUrl)
        return if (match != null) {
            val fileId = match.groupValues[1]
            "https://drive.google.com/uc?export=view&id=$fileId"
        } else {
            originalUrl
        }
    }
    private fun canSelectItem(category: String): Boolean {
        // ตรวจสอบว่าหมวดหมู่นี้สามารถเลือกได้กี่รายการ
        return (selectedItems.filter { it.Category == category }.size < 2)
    }
}
