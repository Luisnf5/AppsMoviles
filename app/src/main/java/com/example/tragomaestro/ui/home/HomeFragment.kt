package com.example.tragomaestro.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.playersFragment)
        }

        binding.btnInstagram.setOnClickListener {
            openUrl("https://www.instagram.com")
        }

        binding.btnFacebook.setOnClickListener {
            openUrl("https://www.facebook.com")
        }

        binding.btnTwitter.setOnClickListener {
            openUrl("https://twitter.com")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}