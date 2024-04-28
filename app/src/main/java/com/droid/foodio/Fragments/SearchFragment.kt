package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.R
import com.droid.foodio.adapter.menuAdapter
import com.droid.foodio.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adapter:menuAdapter
    private val originalMenuFoodName=listOf("Burger","Sandwich","Momos","Garlic Bread","cheesy dip")
    private val originalPrice= listOf("$7","$12","$17","$23","$3")
    private val originalImages= listOf(
        R.drawable.menuphoto,
        R.drawable.photomenu,
        R.drawable.menuphoto,
        R.drawable.photomenu,
        R.drawable.menuphoto)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filteredValueFoodName= mutableListOf<String>()
    private val filteredValueFoodPrices= mutableListOf<String>()
    private val filteredMenuImage= mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        adapter= menuAdapter(filteredValueFoodName,filteredValueFoodPrices,filteredMenuImage
        )

        binding.searchRV.layoutManager= LinearLayoutManager(requireContext())
        binding.searchRV.adapter=adapter

        // setup search view

        setUpSearchView()

        // show all menu tems

        showAllMenu()
        return binding.root
    }

    private fun showAllMenu() {
        filteredValueFoodName.clear()
        filteredValueFoodPrices.clear()
        filteredMenuImage.clear()

        filteredMenuImage.addAll(originalImages)
        filteredValueFoodName.addAll(originalMenuFoodName)
        filteredValueFoodPrices.addAll(originalPrice)

        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {

        filteredValueFoodName.clear()
        filteredValueFoodPrices.clear()
        filteredMenuImage.clear()

        originalMenuFoodName.forEachIndexed{index, foodName ->

            if(foodName.contains(query, ignoreCase = true))
            {
                filteredMenuImage.add(originalImages[index])
                filteredValueFoodName.add(foodName)
                filteredValueFoodPrices.add(originalPrice[index])
            }
        }
        adapter.notifyDataSetChanged()
    }
    companion object{

    }

}