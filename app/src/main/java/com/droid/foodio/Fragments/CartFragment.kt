package com.droid.foodio.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.PayOut1Activity
import com.droid.foodio.adapter.cartAdapter
import com.droid.foodio.databinding.FragmentCartBinding
import com.droid.foodio.utils.cartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var cartAdapter: cartAdapter
    private lateinit var userId: String

    private val foodNames = mutableListOf<String>()
    private val foodPrices = mutableListOf<String>()
    private val foodImages = mutableListOf<String>()
    private val quantities = mutableListOf<Int>()
    private val foodIngredients = mutableListOf<String>()
    private val foodDescriptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        userId = auth.currentUser?.uid ?: run {
            Log.e("CartFragment", "User ID is null")
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return null
        }

        retrieveCartItems()

        binding.buttonProceed.setOnClickListener {
            // get order items details before proceeding to check out

            getOrderItemDetails()

        }

        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdRef : DatabaseReference = database.reference.child("User").child(userId).child("cartItems")


        val  foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        // get item quantities

        val quantities=cartAdapter.getUpdatedItemQuantities()

        // get item details

        orderIdRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {

                for (foodSnapshot in snapshot.children) {
                    val orderItems = foodSnapshot.getValue(cartItems::class.java)
                    // add items details in to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredient.add(it) }
                }
                // pass items details to PayOut1Activity
                orderNow(foodName, foodPrice, foodImage, foodDescription, foodIngredient, quantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data not fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun orderNow(foodName: MutableList<String>, foodPrice: MutableList<String>, foodImage: MutableList<String>, foodDescription: MutableList<String>, foodIngredient: MutableList<String>, quantities: MutableList<Int>) {

        if(isAdded && context!=null)
        {
            val intent = Intent(requireContext(), PayOut1Activity::class.java)
            intent.putExtra("foodItemName",foodName as ArrayList<String>)
            intent.putExtra("foodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("foodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("foodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("foodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("foodItemQuantity",quantities as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("User").child(userId).child("cartItems")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val cartItem = item.getValue(cartItems::class.java)
                        cartItem?.let {
                            foodNames.add(it.foodName ?: "")
                            foodPrices.add(it.foodPrice ?: "")
                            foodImages.add(it.foodImage ?: "")
                            foodDescriptions.add(it.foodDescription ?: "")
                            foodIngredients.add(it.foodIngredients ?: "")
                            quantities.add(it.foodQuantity ?: 1)
                        }
                    }
                    setAdapter()
                } else {
                    Log.e("CartFragment", "No cart items found")
                    Toast.makeText(context, "No cart items found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartFragment", "Failed to fetch data: ${error.message}")
                Toast.makeText(context, "Data not fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter() {
        cartAdapter = cartAdapter(requireContext(), foodNames, foodPrices, foodImages, foodDescriptions, foodIngredients, quantities)
        binding.cartRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRV.adapter = cartAdapter
    }


    companion object {

    }
}