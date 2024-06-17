package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.droid.foodio.R
import com.droid.foodio.adapter.menuAdapter
import com.droid.foodio.databinding.FragmentHomeBinding
import com.droid.foodio.utils.menuItems
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
  private lateinit var binding:FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItem: MutableList<menuItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog=MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        retreiveAndDisplayPopularItems()
        return binding.root
    }

    private fun retreiveAndDisplayPopularItems() {
        // get reference to database
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

                randomItems()
            }




            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun randomItems() {

        val index=menuItem.indices.shuffled()
        val numItemToShow=6
        val randomItems=index.take(numItemToShow).map { menuItem[it] }
        setPopularAdapter(randomItems)
    }

    private fun setPopularAdapter(randomItems: List<menuItems>) {
        val adapter=menuAdapter(randomItems,requireContext())
        binding.recyclerviewPopular.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerviewPopular.adapter=adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var imageList=ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(),itemMessage, Toast.LENGTH_SHORT).show()
            }
        })



    }
}
