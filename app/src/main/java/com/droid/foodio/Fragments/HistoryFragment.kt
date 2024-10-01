package com.droid.foodio.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.droid.foodio.RecentBuyActivity
import com.droid.foodio.adapter.buyAgainAdapter
import com.droid.foodio.databinding.FragmentHistoryBinding
import com.droid.foodio.utils.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: buyAgainAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var database: FirebaseDatabase
    private var listOrderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //get data from firebase
        retrieveOrderItems()

        binding.recentBuyCL.setOnClickListener {
            seeItemsRecentBuy()
        }

        binding.acceptButton.setOnClickListener {
            updateOrderStatus()
        }

        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOrderItem[0].itemPushKey
        val completeOrderRef =database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderRef.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        val recentBuy = listOrderItem.firstOrNull()
        recentBuy?.let {
            val intent = Intent(requireContext(), RecentBuyActivity::class.java)
            intent.putExtra("recentBuy", it)
            startActivity(intent)
        }
    }

    private fun retrieveOrderItems() {
        binding.recentBuyCL.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance()

        val ref: DatabaseReference = database.reference.child("User").child(userId).child("BuyHistory")
        val shortingQuery = ref.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOrderItem.add(it)
                    }
                }
                listOrderItem.reverse()
                if (listOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentBuyCL.visibility = View.VISIBLE
        val recentBuyItem = listOrderItem.firstOrNull()
        recentBuyItem?.let {
            with(binding) {
                name.text = it.foodNames?.firstOrNull() ?: ""
                val prices = it.foodPrices?.firstOrNull() ?: ""
                price.text = "$ $prices"
                val images = it.foodImages?.firstOrNull() ?: ""
                val uri = Uri.parse(images)
                Glide.with(requireContext()).load(uri).into(image)


                val isOrderAccepted = listOrderItem[0].orderAccepted
                if (isOrderAccepted == true) {
//                    orderStatus.background.setTint(android.graphics.Color.GREEN)
                    acceptButton.visibility = View.VISIBLE
                }
//                else {
//                    acceptButton.visibility = View.VISIBLE
//                    orderStatus.background.setTint(android.graphics.Color.RED)
//                }
            }
        }
    }

    private fun setPreviousBuyItemRecyclerView() {
        val foodName = mutableListOf<String>()
        val price = mutableListOf<String>()
        val images = mutableListOf<String>()

        for (i in 1 until listOrderItem.size) {
            listOrderItem[i].foodNames?.firstOrNull()?.let { foodName.add(it) }
            listOrderItem[i].foodPrices?.firstOrNull()?.let {
                price.add(it)
                listOrderItem[i].foodImages?.firstOrNull()?.let {
                    images.add(it)
                }
            }
        }

        val rv = binding.historyRV
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = buyAgainAdapter(foodName, price, images, requireContext())
        rv.adapter = buyAgainAdapter
    }
}
