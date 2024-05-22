package com.droid.foodio.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.foodio.databinding.MenuItemBinding
import com.droid.foodio.detailsActivity

class menuAdapter(private val menuItems:MutableList<String>, private val menuItemPrices: MutableList<String>, private val menuImages:MutableList<Int>,private val requireContext: Context): RecyclerView.Adapter<menuAdapter.menuViewHolder>() {
private val itemClickListener:OnClickListener?=null

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

        init {
            binding.root.setOnClickListener {
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION)
                {
                    itemClickListener?.onItemClick(position)
                }
                val intent= Intent(requireContext, detailsActivity::class.java)
                intent.putExtra("foodName",menuItems[position])
                intent.putExtra("foodPrice",menuItemPrices[position])
                intent.putExtra("foodImage",menuImages[position])
                requireContext.startActivity(intent)
            }
        }
        fun bind(position: Int)
        {
            binding.apply {
                menuFoodName.text=menuItems[position]
                menuPrice.text=menuItemPrices[position]
                menuImage.setImageResource(menuImages[position])


            }

        }
    }
    interface OnClickListener
    {
        fun onItemClick(position: Int)
    }
}




