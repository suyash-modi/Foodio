package com.droid.foodio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.foodio.adapter.RecentBuyAdapter
import com.droid.foodio.databinding.ActivityRecentBuyBinding
import com.droid.foodio.utils.OrderDetails

class RecentBuyActivity : AppCompatActivity() {
    private val binding: ActivityRecentBuyBinding by lazy {
        ActivityRecentBuyBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.menuBackButton.setOnClickListener {
            onBackPressed()
        }

        val recentOrderItem = intent.getSerializableExtra("recentBuy") as OrderDetails?
        recentOrderItem?.let {
            allFoodNames = it.foodNames as ArrayList<String>
            allFoodPrices = it.foodPrices as ArrayList<String>
            allFoodImages = it.foodImages as ArrayList<String>
            allFoodQuantities = it.foodQuantities as ArrayList<Int>
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.historyRV
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoodNames, allFoodImages, allFoodPrices, allFoodQuantities)
        rv.adapter = adapter
    }
}
