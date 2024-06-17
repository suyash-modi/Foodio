package com.droid.foodio.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.foodio.databinding.CardRevBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class cartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val itemImages: MutableList<String>,
    private val cartItemDescription: MutableList<String>,
    private val cartIngredients: MutableList<String>,
    private val cartQuantity: MutableList<Int>,
) : RecyclerView.Adapter<cartAdapter.cartViewHolder>() {

    // initialise firebase
    private val auth= FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemsReference = database.reference.child("User").child(userId).child("cartItems")

    }

    companion object{


        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val binding = CardRevBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getUpdatedItemQuantities(): MutableList<Int> {
        val itemQuantities = mutableListOf<Int>()
        itemQuantities.addAll(cartQuantity)
        return itemQuantities
    }


    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class cartViewHolder(private val binding: CardRevBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantitys = itemQuantities[position]
                cartName.text = cartItems[position]
                val price = "$ "+cartItemPrices[position]
                cartItemPrice.text = price
                val uriString=itemImages[position]
                Log.d("image","food Url:$uriString")
                val uri=Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)
                quantity.text =quantitys.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {

                    val itemPos = adapterPosition
                    if (itemPos != RecyclerView.NO_POSITION)
                        deleteItem(itemPos)
                }

            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--;
                cartQuantity[position]=itemQuantities[position]
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++;
                cartQuantity[position]=itemQuantities[position]
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
                if (uniqueKey!=null) {
                    removeItem(position,uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey!=null) {
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartItemPrices.removeAt(position)
                    itemImages.removeAt(position)
                    cartItemDescription.removeAt(position)
                    cartIngredients.removeAt(position)
                    cartQuantity.removeAt(position)

                    // update itemQuantities
                    itemQuantities =
                        itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                    Toast.makeText(context, "item removed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "item not removed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete : (String?) -> Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey : String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

}