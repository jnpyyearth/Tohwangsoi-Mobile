package com.example.tohwangsoi_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.adapter.MyorderAdapter
import com.example.tohwangsoi_mobile.model.Myorder
import androidx.recyclerview.widget.LinearLayoutManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyOrderFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyorderAdapter
    private lateinit var orderList: ArrayList<Myorder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myorder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewOrders)

        // สร้างรายการจำลอง
        orderList = arrayListOf(
                Myorder("1", "Package 2500", "2,500 บาท", "21 มีนาคม 2025", "ชลบุรี", "มหาวิทยาลัยเกษตรศาสตร์ ศรีราชา", "100 คน"),
                Myorder("2", "Package 3000", "3,000 บาท", "22 มีนาคม 2025", "กรุงเทพ", "อิมแพค เมืองทองธานี", "120 คน")
            )

        adapter = MyorderAdapter(orderList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}

