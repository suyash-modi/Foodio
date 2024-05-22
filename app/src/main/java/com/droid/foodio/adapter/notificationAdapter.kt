package com.droid.foodio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.foodio.databinding.NotificationRvBinding

class notificationAdapter(private var notifications:ArrayList<String>,private var notificationImage:ArrayList<Int>) : RecyclerView.Adapter<notificationAdapter.notificationViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificationViewHolder {
        val binding= NotificationRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return notificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: notificationViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class notificationViewHolder(private val binding:NotificationRvBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notificationtextViewn.text=notifications[position]
                notificationImagen.setImageResource(notificationImage[position])
            }
        }

    }

}