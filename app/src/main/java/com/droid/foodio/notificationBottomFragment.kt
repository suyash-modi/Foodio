package com.droid.foodio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.adapter.notificationAdapter
import com.droid.foodio.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class notificationBottomFragment : BottomSheetDialogFragment() {
private lateinit var binding: FragmentNotificationBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val foodName= listOf("Burger","Sandwich","Momos","Garlic Bread","cheesy dip")
        val images= listOf(
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto)
        val adapter=notificationAdapter(ArrayList(foodName),ArrayList(images))
        binding.notificationRV.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRV.adapter=adapter
        return binding.root
    }

}