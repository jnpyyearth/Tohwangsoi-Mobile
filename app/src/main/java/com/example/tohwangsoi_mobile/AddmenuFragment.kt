import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.adapter.MenuManagerAdapter
import com.example.tohwangsoi_mobile.databinding.DialogAddMenuBinding
import com.example.tohwangsoi_mobile.databinding.FragmentAddmenuBinding
import com.example.tohwangsoi_mobile.model.MenuManagerItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AddmenuFragment : Fragment() {

    private lateinit var binding: FragmentAddmenuBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: SectionedMenuAdapter
    private val menuList = mutableListOf<MenuManagerItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddmenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.firestore
        Log.d("FragmentLifecycle", "AddmenuFragment ถูกสร้างแล้ว")
        // ตั้งค่า RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (menuList.isEmpty()) {
                    Log.e("R", "menuList ว่างเปล่า ไม่มีข้อมูล")
                    return 2 // เผื่อไม่ให้แอปพัง
                }
                val spanSize = if (menuList[position].isHeader) 2 else 1
                Log.d("R", "Position: $position, Name: ${menuList[position].MenuName}, isHeader: ${menuList[position].isHeader}, SpanSize: $spanSize")
                return spanSize
            }
        }
        binding.recyclerViewMenu.layoutManager = layoutManager
        Log.d("RecyclerView", " RecyclerView ใช้ LayoutManager: ${binding.recyclerViewMenu.layoutManager}")
        adapter = SectionedMenuAdapter(menuList,
            onEditClick = { menu -> showEditMenuDialog(menu) },  //  แก้ไขเมนู
            onDeleteClick = { menu -> showDeleteMenuDialog(menu) }  //  ลบเมนู
        )
        binding.recyclerViewMenu.adapter = adapter
        Log.d("RecyclerView", " RecyclerView Visibility: ${binding.recyclerViewMenu.visibility}")

        Log.d("Firestore", "เรียก loadMenuData()")
        loadMenuData()

        // ตั้งค่าปุ่ม FloatingActionButton กดแล้วเปิด Dialog
        binding.buttonAdd.setOnClickListener {
            showAddMenuDialog()
        }
    }

    private fun showAddMenuDialog() {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogAddMenuBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)

        dialogBinding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonConfirm.setOnClickListener {
            val menuName = dialogBinding.editTextMenuName.text.toString().trim()
            val imageUrl = dialogBinding.editTextImageLink.text.toString().trim()
            val selectedCategoryId = dialogBinding.radioGroupCategory.checkedRadioButtonId

            val category = when (selectedCategoryId) {
                R.id.radioAppetizer -> "เรียกน้ำย่อย"
                R.id.radioMainCourse -> "จานหลัก"
                R.id.radioDessert -> "ของหวาน"
                R.id.radioDrink -> "เครื่องดื่ม"
                else -> ""
            }

            if (menuName.isEmpty() || category.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(requireContext(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val datamenu = hashMapOf(
                "MenuName" to menuName,
                "Category" to category,
                "Image" to imageUrl,
                "timestamp" to FieldValue.serverTimestamp()
            )

            database.collection("Menu")
                .add(datamenu)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "เมนูบันทึกสำเร็จ", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadMenuData() // โหลดเมนูใหม่ทันที
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "ไม่สามารถบันทึกเมนูได้: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        dialog.show()
    }

    private fun loadMenuData() {
        database.collection("Menu")
            .get()
            .addOnSuccessListener { result ->
                val menuMap = mutableMapOf<String, MutableList<MenuManagerItem>>()

                for (document in result) {
                    val menu = document.toObject(MenuManagerItem::class.java).copy(id = document.id)
                    if (!menuMap.containsKey(menu.Category)) {
                        menuMap[menu.Category] = mutableListOf()
                    }
                    menuMap[menu.Category]?.add(menu)
                }

                menuList.clear()
                for ((category, items) in menuMap) {
                    val header = MenuManagerItem(id ="",MenuName = category, Category = "", Image = "", isHeader = true)
                    menuList.add(header)
                    menuList.addAll(items)
                }

                Log.d("Firestore", "menuList มีข้อมูล ${menuList.size} รายการ")
                menuList.forEachIndexed { index, item ->
                    Log.d("Firestore", " Position: $index, Name: ${item.MenuName}, isHeader: ${item.isHeader}")
                }

                adapter.notifyDataSetChanged()
                Log.d("RecyclerView", " เรียก notifyDataSetChanged() แล้ว, RecyclerView ควรเริ่ม Render")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", " โหลดข้อมูลจาก Firestore ล้มเหลว", e)
            }
    }
    private fun showDeleteMenuDialog(menu: MenuManagerItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("ลบเมนู")
            .setMessage("คุณแน่ใจหรือไม่ว่าต้องการลบเมนูนี้?")
            .setPositiveButton("ลบ") { _, _ ->
                FirebaseFirestore.getInstance().collection("Menu")
                    .document(menu.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "ลบเมนูสำเร็จ", Toast.LENGTH_SHORT).show()
                        menuList.remove(menu)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "ลบไม่สำเร็จ: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("ยกเลิก", null)
            .show()
    }
    private fun showEditMenuDialog(menuItem: MenuManagerItem) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogAddMenuBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)

        // ตั้งค่าให้ EditText แสดงชื่อเมนูเดิม
        dialogBinding.editTextMenuName.setText(menuItem.MenuName)

        //  ซ่อนช่องอัปโหลดรูปภาพ (เพราะไม่ได้แก้ไขรูป)
        dialogBinding.editTextImageLink.visibility = View.GONE

        dialogBinding.buttonCancel.setOnClickListener { dialog.dismiss() }

        dialogBinding.buttonConfirm.setOnClickListener {
            val newMenuName = dialogBinding.editTextMenuName.text.toString().trim()

            if (newMenuName.isEmpty()) {
                Toast.makeText(requireContext(), "กรุณากรอกชื่อเมนู", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedData = hashMapOf<String, Any>(
                "MenuName" to newMenuName
            )

            FirebaseFirestore.getInstance().collection("Menu")
                .document(menuItem.id) // ✅ ใช้ ID ของ Firebase
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "อัปเดตชื่อเมนูสำเร็จ", Toast.LENGTH_SHORT).show()
                    menuItem.MenuName = newMenuName // ✅ อัปเดตข้อมูลใน List
                    adapter.notifyDataSetChanged() // ✅ รีเฟรช RecyclerView
                    dialog.dismiss()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "แก้ไขไม่สำเร็จ: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        dialog.show()
    }
}
