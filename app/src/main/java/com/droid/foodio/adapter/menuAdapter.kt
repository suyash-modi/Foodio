package com.droid.foodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.foodio.databinding.MenuItemBinding

class menuAdapter(private val menuItems:MutableList<String>, private val menuItemPrices: MutableList<String>, private val menuImages:MutableList<Int>): RecyclerView.Adapter<menuAdapter.menuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val binding=MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return menuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class menuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int)
        {
            binding.apply {
                menuFoodName.text=menuItems[position]
                menuPrice.text=menuItemPrices[position]
                menuImage.setImageResource(menuImages[position])
            }

        }
    }
}