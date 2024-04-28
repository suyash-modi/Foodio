package com.droid.foodio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.droid.foodio.databinding.ActivityChooseLocationBinding


class ChooseLocationActivity : AppCompatActivity() {
    val binding:ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationList= arrayListOf<String>("Jaipur","Jhansi","Lucknow","Gonda")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.listItem
        autoCompleteTextView.setAdapter(adapter)
    }
}