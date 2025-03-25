package com.example.tohwangsoi_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tohwangsoi_mobile.adapter.OrderAdapter
import com.example.tohwangsoi_mobile.databinding.FragmentOrderBinding

import com.example.tohwangsoi_mobile.mode.MockData

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderAdapter(MockData.orders)
        binding.recyclerViewOrders.adapter = adapter
    }
}
