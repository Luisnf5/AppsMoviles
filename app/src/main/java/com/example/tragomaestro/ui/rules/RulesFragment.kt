package com.example.tragomaestro.ui.rules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentRulesBinding

class RulesFragment : Fragment(R.layout.fragment_rules) {

    private var _binding: FragmentRulesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRulesBinding.bind(view)

        binding.btnCloseRules.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGotIt.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}