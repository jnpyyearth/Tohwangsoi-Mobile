package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tohwangsoi_mobile.databinding.FragmentHomecustomerBinding

class HomeCustomerFragment : Fragment() {

    private lateinit var binding: FragmentHomecustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomecustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.package2000.setOnClickListener{
            (activity as MainActivity).replaceFragment(UserOrderDataFragment())
        }
        binding.package2500.setOnClickListener{
            (activity as MainActivity).replaceFragment(UserOrderDataFragment())
        }
        binding.package3000.setOnClickListener{
            (activity as MainActivity).replaceFragment(UserOrderDataFragment())
        }

    }
}
