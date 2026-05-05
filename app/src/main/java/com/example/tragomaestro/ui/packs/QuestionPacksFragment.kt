package com.example.tragomaestro.ui.packs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentQuestionPacksBinding
import com.example.tragomaestro.repository.QuestionPackRepository
import com.example.tragomaestro.viewmodel.QuestionPacksViewModel
import com.example.tragomaestro.viewmodel.QuestionPacksViewModelFactory
import timber.log.Timber

class QuestionPacksFragment : Fragment(R.layout.fragment_question_packs) {

    private var _binding: FragmentQuestionPacksBinding? = null
    private val binding get() = _binding!!

    private val adapter = QuestionPacksAdapter { pack ->
        viewModel.setPackSelected(pack.id, !pack.isSelected)
    }

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

        _binding = FragmentQuestionPacksBinding.bind(view)
        Timber.i("QuestionPacksFragment cargado")

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvQuestionPacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuestionPacks.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnClosePacks.setOnClickListener {
            findNavController().popBackStack(R.id.playersFragment, false)
        }

        binding.btnSavePacks.setOnClickListener {
            Timber.i("Selección de packs guardada")
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.packs.observe(viewLifecycleOwner) { packs ->
            adapter.submitList(packs)

            val selectedCount = packs.count { it.isSelected }
            binding.tvSelectedPacksCount.text = "$selectedCount PACKS SELECCIONADOS"

            Timber.d("Packs cargados: ${packs.size}, seleccionados=$selectedCount")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("QuestionPacksFragment destruido")
        _binding = null
    }
}