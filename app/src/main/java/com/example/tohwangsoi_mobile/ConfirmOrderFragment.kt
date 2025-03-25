package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tohwangsoi_mobile.databinding.FragmentConfirmOrderBinding
import com.example.tohwangsoi_mobile.databinding.FragmentUserOrderDataBinding

class ConfirmOrderFragment : Fragment() {

    private lateinit var binding: FragmentConfirmOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  ตรงนี้ใส่ได้เลย
        binding.btnCancel.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(HomeCustomerFragment())
        }

        // ปุ่มถัดไป (ถ้ามี)
        binding.btnApprove.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(SelectMenuFragment())
        }
    }
}
