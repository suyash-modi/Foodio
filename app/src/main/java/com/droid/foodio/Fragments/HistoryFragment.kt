package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.R
import com.droid.foodio.adapter.buyAgainAdapter
import com.droid.foodio.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding:FragmentHistoryBinding
    private lateinit var BuyAgainAdapter:buyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)

        setupRecyclerView()

        return binding.root
    }
    private fun setupRecyclerView()
    {
        val foodName= listOf("Burger","Sandwich","Momos","Garlic Bread","cheesy dip")
        val price= listOf("$7","$12","$17","$23","$3")
        val images= listOf(
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto)

        val adapter= buyAgainAdapter(ArrayList(foodName),ArrayList(price),ArrayList(images))
        binding.historyRV.layoutManager= LinearLayoutManager(requireContext())
        binding.historyRV.adapter=adapter

    }

}