package com.example.tohwangsoi_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tohwangsoi_mobile.databinding.FragmentPaymentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : Fragment() {
    private lateinit var orderNo: String
    private lateinit var packageName: String
    private lateinit var price: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ดึงข้อมูลจาก bundle ที่ส่งมา
        arguments?.let {
            orderNo = it.getString("orderNo", "")
            packageName = it.getString("packageName", "")
            price = it.getString("price", "")
        }

        // แสดงข้อมูลการชำระเงิน
        val binding: FragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater)
        binding.tvOrderNo.text = "Order No: $orderNo"
        binding.tvPackageName.text = "Package: $packageName"
        binding.tvPrice.text = "Price: $price"

        return binding.root
    }
}
