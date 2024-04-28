package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.R
import com.droid.foodio.adapter.menuAdapter
import com.droid.foodio.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMenuBottomSheetBinding.inflate(layoutInflater,container,false)

        binding.menuBackButton.setOnClickListener {
            dismiss()
        }


        val foodName= listOf("Burger","Sandwich","Momos","Garlic Bread","cheesy dip")
        val price= listOf("$7","$12","$17","$23","$3")
        val images= listOf(
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto)

        val adapter= menuAdapter(ArrayList(foodName),ArrayList(price),ArrayList(images))
        binding.menuBottomRV.layoutManager= LinearLayoutManager(requireContext())
        binding.menuBottomRV.adapter=adapter

        return binding.root
    }


}