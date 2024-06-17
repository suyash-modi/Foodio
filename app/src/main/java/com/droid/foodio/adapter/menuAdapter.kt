package com.droid.foodio.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.foodio.databinding.MenuItemBinding
import com.droid.foodio.detailsActivity
import com.droid.foodio.utils.menuItems

class menuAdapter(private val menuItems: List<menuItems>, private val requireContext: Context): RecyclerView.Adapter<menuAdapter.menuViewHolder>() {


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
                    openDetailActivity(position)
                }

            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem=menuItems[position]

            // a intent to pass data in detailsActivity
            val intent=Intent(requireContext,detailsActivity::class.java).apply {
                putExtra("menuItemName",menuItem.foodName)
                putExtra("menuItemPrice",menuItem.foodPrice)
                putExtra("menuItemImage",menuItem.foodImage)
                putExtra("menuItemDescription",menuItem.foodDescription)
                putExtra("menuItemIngredients",menuItem.foodIngredient)

            }
            // start the activity
            requireContext.startActivity(intent)

        }


        // bind the data to the views
        fun bind(position: Int)
        {
            val menuItem=menuItems[position]

            binding.apply {
                menuFoodName.text=menuItem.foodName
                menuPrice.text="$"+menuItem.foodPrice

                val uri=Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }

        }
    }

}




