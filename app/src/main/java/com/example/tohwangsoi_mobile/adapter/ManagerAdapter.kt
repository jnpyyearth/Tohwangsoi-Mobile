package com.example.tohwangsoi_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tohwangsoi_mobile.R
import com.example.tohwangsoi_mobile.model.Manager

class ManagerAdapter(private val managerList: List<Manager>) :
    RecyclerView.Adapter<ManagerAdapter.ManagerViewHolder>() {

    class ManagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFullName: TextView = view.findViewById(R.id.txtFullName)
        val txtRole: TextView = view.findViewById(R.id.txtRole)
        val txtEmail: TextView = view.findViewById(R.id.txtEmail)
        val txtPassword: TextView = view.findViewById(R.id.txtPassword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager, parent, false)
        return ManagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ManagerViewHolder, position: Int) {
        val manager = managerList[position]
        holder.txtFullName.text = manager.fullName
        holder.txtRole.text = manager.role
        holder.txtEmail.text = manager.email
        holder.txtPassword.text = manager.password
    }

    override fun getItemCount(): Int {
        return managerList.size
    }
}
