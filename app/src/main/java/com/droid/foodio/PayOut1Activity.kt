package com.droid.foodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.droid.foodio.databinding.ActivityPayOut1Binding

class PayOut1Activity : AppCompatActivity() {
    lateinit var binding: ActivityPayOut1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityPayOut1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.placeOrderButton.setOnClickListener{
            val bottomSheetDialog=CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }
}