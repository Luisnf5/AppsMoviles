package com.example.tragomaestro.ui.packs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentCreateCustomPackBinding
import com.example.tragomaestro.repository.QuestionPackRepository
import com.example.tragomaestro.viewmodel.QuestionPacksViewModel
import com.example.tragomaestro.viewmodel.QuestionPacksViewModelFactory
import timber.log.Timber

class CreateCustomPackFragment : Fragment(R.layout.fragment_create_custom_pack) {

    private var _binding: FragmentCreateCustomPackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuestionPacksViewModel by viewModels {
        val database = (requireActivity().application as TragoMaestroApplication).database
        val repository = QuestionPackRepository(
            packDao = database.questionPackDao(),
            questionDao = database.questionDao()
        )
        QuestionPacksViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCreateCustomPackBinding.bind(view)

        binding.btnCloseCreatePack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCreatePack.setOnClickListener {
            val name = binding.etPackName.text.toString().trim()
            val description = binding.etPackDescription.text.toString().trim()

            if (name.isBlank() || description.isBlank()) {
                Toast.makeText(requireContext(), getString(R.string.packs_error_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createPack(name, description)
            Timber.i("Pack personalizado creado desde UI: $name")
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}