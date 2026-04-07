package com.example.tragomaestro.ui.rules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentRulesBinding
import timber.log.Timber

class RulesFragment : Fragment(R.layout.fragment_rules) {

    private var _binding: FragmentRulesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i("RulesFragment cargado")
        _binding = FragmentRulesBinding.bind(view)

        binding.btnCloseRules.setOnClickListener {
            Timber.i("Botón cerrar pulsado en RulesFragment")
            findNavController().navigateUp()
        }

        binding.btnGotIt.setOnClickListener {
            Timber.i("Botón 'Lo tengo' pulsado en RulesFragment")
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        Timber.i("Destruyendo vista de RulesFragment")
        super.onDestroyView()
        _binding = null
    }
}