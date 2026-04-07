package com.example.tragomaestro.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tragomaestro.R

class RulesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Error: Se arregla cuando añadas el xml
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }
}