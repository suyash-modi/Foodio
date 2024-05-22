package com.droid.foodio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.droid.foodio.databinding.ActivityDetailsBinding

class detailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("foodName")
        val foodImage = intent.getIntExtra("foodImage",0)
        binding.foodNameTxt.text = foodName
        binding.foodImg.setImageResource(foodImage)

        binding.menuBackButton.setOnClickListener {
            onBackPressed()
        }

    }
}