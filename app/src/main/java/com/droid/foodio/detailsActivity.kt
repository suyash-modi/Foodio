package com.droid.foodio

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.droid.foodio.databinding.ActivityDetailsBinding
import com.droid.foodio.utils.cartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class detailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private  var foodName: String?=null
    private  var foodImage: String?=null
    private  var foodDescription: String?=null
    private  var foodIngredients: String?=null
    private  var foodPrice: String?=null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        foodName = intent.getStringExtra("menuItemName")
        foodImage = intent.getStringExtra("menuItemImage")
        foodDescription = intent.getStringExtra("menuItemDescription")
        foodIngredients = intent.getStringExtra("menuItemIngredients")
        foodPrice = intent.getStringExtra("menuItemPrice")

        with(binding){
            foodNameTxt.text = foodName
            Glide.with(this@detailsActivity).load(Uri.parse(foodImage)).into(foodImg)
            descriptionTxt.text = foodDescription
            foodIg.text = foodIngredients
        }


        binding.menuBackButton.setOnClickListener {
            onBackPressed()
        }

        binding.buttonProceed.setOnClickListener {
            addItemsToCart()
        }

    }

    private fun addItemsToCart() {
        val database =FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        val cartItem = cartItems(foodName.toString(),foodPrice.toString(),foodImage.toString(),foodDescription.toString(),foodIngredients,1)

        // save data to firebase

        database.child("User").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            binding.buttonProceed.isEnabled = false
            binding.buttonProceed.text = "Added to cart"
            binding.buttonProceed.setBackgroundColor(R.drawable.menu_shape)
            binding.buttonProceed.setTextColor(resources.getColor(R.color.green))
            Toast.makeText(this, "item added into cart successfully", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "item not added into cart", Toast.LENGTH_SHORT).show()
        }

        }
}