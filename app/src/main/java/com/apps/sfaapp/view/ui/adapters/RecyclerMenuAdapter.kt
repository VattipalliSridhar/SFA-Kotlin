package com.apps.sfaapp.view.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.sfaapp.databinding.MenuAdapterBinding
import com.apps.sfaapp.model.MenuModel

class RecyclerMenuAdapter(
    private val applicationContext: Context,
    private val menuModel: ArrayList<MenuModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerMenuAdapter.MyHolderClass>() {

    inner  class MyHolderClass(var menuAdapterBinding: MenuAdapterBinding) : RecyclerView.ViewHolder(menuAdapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderClass {
        val view = MenuAdapterBinding.inflate(
            LayoutInflater.from(applicationContext),
            parent,
            false)
        return  MyHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyHolderClass, position: Int) {

        holder.menuAdapterBinding.textName.text = menuModel[position].name
        holder.menuAdapterBinding.linear.setOnClickListener {
            val position = holder.adapterPosition
            Log.e("msg", "" + position)
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return menuModel.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}