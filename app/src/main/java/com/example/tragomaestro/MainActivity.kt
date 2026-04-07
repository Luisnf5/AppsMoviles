package com.example.tragomaestro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tragomaestro.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate de MainActivity")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart de MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume de MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause de MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop de MainActivity")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart de MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy de MainActivity")
    }
}