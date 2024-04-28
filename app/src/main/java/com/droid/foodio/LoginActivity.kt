package com.droid.foodio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.droid.foodio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private  val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.dontbtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.loginbtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}