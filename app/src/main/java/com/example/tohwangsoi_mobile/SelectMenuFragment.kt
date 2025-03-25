package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tohwangsoi_mobile.databinding.FragmentSelectMenuBinding

class SelectMenuFragment : Fragment() {

    private lateinit var binding: FragmentSelectMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔙 ปุ่มยกเลิก → กลับหน้า Home
        binding.btnCancel.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(HomeCustomerFragment())
        }
        binding.btnNext.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(ConfirmOrderFragment())
        }
    }
}
