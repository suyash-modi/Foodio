package com.droid.foodio.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.foodio.databinding.PopularRevBinding
import com.droid.foodio.detailsActivity

class popularAdapter (private val items:List<String>,private val prices:List<String>,private val images:List<Int>,private val requireContext: Context): RecyclerView.Adapter<popularAdapter.popularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularViewHolder {
        return popularViewHolder(PopularRevBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: popularViewHolder, position: Int) {
        val item=items[position]
        val image=images[position]
        val price=prices[position]
        holder.bind(item,image,price)

        holder.itemView.setOnClickListener {
            val intent= Intent(requireContext, detailsActivity::class.java)
            intent.putExtra("foodName",item)
            intent.putExtra("foodImage",image)
            requireContext.startActivity(intent)
        }
    }

    class popularViewHolder(private val binding: PopularRevBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, image: Int,price:String) {

            binding.imagePopular.setImageResource(image)
            binding.namePopular.text=item
            binding.pricePopular.text=price
        }

    }
}