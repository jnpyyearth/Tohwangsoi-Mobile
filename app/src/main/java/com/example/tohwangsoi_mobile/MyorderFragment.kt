package com.example.tohwangsoi_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.adapter.MyorderAdapter
import com.example.tohwangsoi_mobile.adapter.OnOrderActionListener

import com.example.tohwangsoi_mobile.model.Myorder
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast

class MyOrderFragment : Fragment(), OnOrderActionListener {

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
            Myorder("1", "Package 2500", "2,500 บาท", "21 มีนาคม 2025", "ชลบุรี", "มหาวิทยาลัยเกษตรศาสตร์ ศรีราชา", "100 คน", isApproved = false),
            Myorder("2", "Package 3000", "3,000 บาท", "22 มีนาคม 2025", "กรุงเทพ", "อิมแพค เมืองทองธานี", "120 คน", isApproved = true),
            Myorder("3", "Package 3500", "2,000 บาท", "23 มีนาคม 2025", "เชียงใหม่", "ศูนย์ประชุมเชียงใหม่", "150 คน", isApproved = true)
        )
        adapter = MyorderAdapter(orderList, this) // ส่ง listener ไปยัง Adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    // ฟังก์ชันที่ใช้เมื่อคลิกปุ่ม "ยกเลิกออเดอร์"

    override fun onCancelOrder(position: Int) {
        // แสดงข้อความหรือการทำงานเมื่อกดปุ่ม "ยกเลิก"
        Toast.makeText(requireContext(), "ยกเลิกออเดอร์ที่ $position", Toast.LENGTH_SHORT).show()
        orderList[position].isCancell = true  // เปลี่ยนสถานะการยกเลิกใน orderList
        adapter.notifyItemChanged(position)  // อัปเดต RecyclerView
    }

    // ฟังก์ชันที่ใช้เมื่อคลิกปุ่ม "จ่ายเงิน"
    // ฟังก์ชันที่ใช้เมื่อคลิกปุ่ม "จ่ายเงิน"
    override fun onPayOrder(position: Int) {
        // ส่งข้อมูลไปยัง PaymentFragment
        val bundle = Bundle().apply {
            putString("orderNo", orderList[position].orderNo)
            putString("packageName", orderList[position].packageName)
            putString("price", orderList[position].price)
        }

        val paymentFragment = PaymentFragment()
        paymentFragment.arguments = bundle

        // เปลี่ยนไปยัง PaymentFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, paymentFragment)
            .addToBackStack(null)
            .commit()
    }

}
