package com.example.tohwangsoi_mobile



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tohwangsoi_mobile.databinding.FragmentSelectMenuBinding
import com.example.tohwangsoi_mobile.model.MenuManagerItem
import com.google.firebase.firestore.FirebaseFirestore

class SelectMenuFragment : Fragment() {

    private lateinit var binding: FragmentSelectMenuBinding
    private lateinit var adapter: SectionedMenuSelectAdapter
    private val menuList = mutableListOf<MenuManagerItem>()
    private val selectedMenuList = mutableListOf<MenuManagerItem>()
    private val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(HomeCustomerFragment())
        }

        binding.btnNext.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(ConfirmOrderFragment())
        }

        setupRecyclerView()
        loadMenuData()
    }

    private fun setupRecyclerView() {
        adapter = SectionedMenuSelectAdapter(menuList) { selectedItems ->
            selectedMenuList.clear()
            selectedMenuList.addAll(selectedItems)
        }

        binding.recyclerViewMenu.layoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (menuList[position].isHeader) 2 else 1
                }
            }
        }
        binding.recyclerViewMenu.adapter = adapter
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
                    val header = MenuManagerItem(id = "", MenuName = category, Category = "", Image = "", isHeader = true)
                    menuList.add(header)
                    menuList.addAll(items)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
