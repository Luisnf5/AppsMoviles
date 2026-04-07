package com.example.tragomaestro.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i("HomeFragment cargado")
        _binding = FragmentHomeBinding.bind(view)

        binding.btnStart.setOnClickListener {
            Timber.i("Botón empezar pulsado, navegando a PlayersFragment")
            findNavController().navigate(R.id.playersFragment)
        }

        binding.btnInstagram.setOnClickListener {
            Timber.i("Botón Instagram pulsado")
            openUrl("https://www.instagram.com")
        }

        binding.btnFacebook.setOnClickListener {
            Timber.i("Botón Facebook pulsado")
            openUrl("https://www.facebook.com")
        }

        binding.btnTwitter.setOnClickListener {
            Timber.i("Botón Twitter pulsado")
            openUrl("https://twitter.com")
        }
    }

    private fun openUrl(url: String) {
        Timber.d("Abriendo URL: $url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        Timber.i("Destruyendo vista de HomeFragment")
        super.onDestroyView()
        _binding = null
    }
}