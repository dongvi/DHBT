package com.vnd.dhbt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vnd.dhbt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startFlowScreen()
    }

    private fun startFlowScreen() {
        loopMeow()
    }

    private fun loopMeow() {
        binding.apply {
            root.postDelayed(
                {
                    meow.text = if(meow.text.isNullOrEmpty() || meow.text == ":3") ":D" else ":3"
                    loopMeow()
                },
                222
            )
        }
    }
}
