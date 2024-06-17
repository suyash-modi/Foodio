package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.adapter.menuAdapter
import com.droid.foodio.databinding.FragmentMenuBottomSheetBinding
import com.droid.foodio.utils.menuItems
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItem: MutableList<menuItems>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(layoutInflater, container, false)

        binding.menuBackButton.setOnClickListener {
            dismiss()
        }

        retreiveItems()

        return binding.root
    }

    private fun retreiveItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        menuItem = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val menu = item.getValue(menuItems::class.java)
                    menu?.let { menuItem.add(it) }
                }
                // one data received set it to adapter

                setMenuAdapter()
            }



            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun setMenuAdapter() {
        val adapter = menuAdapter(menuItem, requireContext())
        binding.menuBottomRV.layoutManager = LinearLayoutManager(requireContext())
        binding.menuBottomRV.adapter = adapter
    }


}