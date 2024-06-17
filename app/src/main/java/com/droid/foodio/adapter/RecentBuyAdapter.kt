package com.droid.foodio.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.foodio.databinding.RecentBuyRvBinding

class RecentBuyAdapter(private var context: Context,
                       private var foodNameList: ArrayList<String>,
                       private var foodImageList: ArrayList<String>,
                       private var foodPriceList: ArrayList<String>,
                       private var foodQuantityList: ArrayList<Int>
    ): RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyViewHolder {
        var binding =RecentBuyRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentBuyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodNameList.size
    }

    override fun onBindViewHolder(holder: RecentBuyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentBuyViewHolder(private val binding: RecentBuyRvBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                name.text = foodNameList[position]
                pricePopular.text = "₹ " + foodPriceList[position]
                quantity.text = foodQuantityList[position].toString()
                val uri=foodImageList[position]
                val url= Uri.parse(uri)
                Glide.with(context).load(url).into(image)
            }
        }

    }

}