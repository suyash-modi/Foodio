package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.adapter.menuAdapter
import com.droid.foodio.databinding.FragmentSearchBinding
import com.droid.foodio.utils.menuItems
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adapter:menuAdapter
    private val originalMenuItems= mutableListOf<menuItems>()
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(layoutInflater,container,false)



        // retrieve data from firebase

        retrieveData()
        // setup search view

        setUpSearchView()


        return binding.root
    }

    private fun retrieveData() {
        database= FirebaseDatabase.getInstance()
        val databaseRef: DatabaseReference =database.reference.child("menu")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children)
                {
                    val menu=data.getValue(menuItems::class.java)
                    menu?.let {
                        originalMenuItems.add(it)
                    }
                    showAllMenu()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItems=ArrayList(originalMenuItems)
        setAdapter(filteredMenuItems)

    }

    private fun setAdapter(filteredMenuItems: List<menuItems>) {
        adapter= menuAdapter(filteredMenuItems,requireContext())
        binding.searchRV.layoutManager=LinearLayoutManager(requireContext())
        binding.searchRV.adapter=adapter
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

        val filteredMenuItems=originalMenuItems.filter {
            it.foodName?.contains(query,ignoreCase = true)==true

        }
        setAdapter(filteredMenuItems)
    }
    companion object{

    }

}