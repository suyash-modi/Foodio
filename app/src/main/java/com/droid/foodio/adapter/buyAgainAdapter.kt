package com.droid.foodio.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.foodio.databinding.BuyAgainRevBinding

class buyAgainAdapter(private val buyAgainFoodName:MutableList<String>, private val buyAgainFoodPrice: MutableList<String>, private val buyAgainFoodImage:MutableList<String>,private val requireContext:Context) :RecyclerView.Adapter<buyAgainAdapter.buyAgainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): buyAgainViewHolder {
        val binding=BuyAgainRevBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return buyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    override fun onBindViewHolder(holder: buyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position])
    }

     inner class buyAgainViewHolder(private val binding: BuyAgainRevBinding):RecyclerView.ViewHolder(binding.root) {
         fun bind(foodName: String, foodPrice: String, foodImage: String) {
             binding.buyAgainName.text=foodName
             binding.buyAgainPrice.text="$ $foodPrice"
//             binding.buyAgainImage.setImageResource(foodImage)
             val images=foodImage.toString()
             val uri= Uri.parse(images)
             Glide.with(requireContext).load(uri).into(binding.buyAgainImage)

         }


     }
}