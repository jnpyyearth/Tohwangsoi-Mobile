import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.databinding.DialogAddMenuBinding
import com.example.tohwangsoi_mobile.model.MenuManagerItem
import com.google.firebase.firestore.FirebaseFirestore

class SectionedMenuAdapter(private val menuList: List<MenuManagerItem>,  private val onEditClick: (MenuManagerItem) -> Unit,   // ✅ เพิ่มฟังก์ชัน Edit
                           private val onDeleteClick: (MenuManagerItem) -> Unit ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategoryHeader)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMenu: ImageView = view.findViewById(R.id.imgMenu)
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)   //  ปุ่ม Edit
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = if (menuList[position].isHeader) TYPE_HEADER else TYPE_ITEM
        Log.d("Adapter", "Position: $position, Name: ${menuList[position].MenuName}, isHeader: ${menuList[position].isHeader}, ViewType: $viewType,")
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menumanger, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = menuList[position]

        if (holder is HeaderViewHolder) {
            holder.tvCategory.text = item.MenuName //  ใช้ `MenuName` เป็นชื่อหมวดหมู่
        } else if (holder is ItemViewHolder) {
            holder.tvMenuName.text = item.MenuName

            val imageUrl = convertGoogleDriveUrl(item.Image) //  แปลง URL ก่อนโหลดภาพ

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground) //  แสดงระหว่างโหลด
                 //  แสดงถ้าภาพโหลดไม่ได้
                .into(holder.imgMenu)
            holder.btnEdit.setOnClickListener {
                Log.d("Adapter", "กด Edit ที่: ${item.MenuName}")
                onEditClick(item)
            }
            holder.btnDelete.setOnClickListener {
                Log.d("Adapter", "กด Delete ที่: ${item.MenuName}")
                onDeleteClick(item)
            }

        }
    }

    override fun getItemCount(): Int = menuList.size

    //  ฟังก์ชันแปลง Google Drive URL
    private fun convertGoogleDriveUrl(originalUrl: String): String {
        val regex = Regex("https://drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)/view\\?usp=sharing")
        val match = regex.find(originalUrl)

        return if (match != null) {
            val fileId = match.groupValues[1]
            "https://drive.google.com/uc?export=view&id=$fileId"
        } else {
            originalUrl // ถ้า URL ไม่ใช่ของ Google Drive ให้คืนค่าเดิม
        }
    }


}

