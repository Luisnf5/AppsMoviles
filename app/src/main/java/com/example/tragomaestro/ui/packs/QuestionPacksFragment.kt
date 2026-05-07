package com.example.tragomaestro.ui.packs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
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

    private val adapter = QuestionPacksAdapter(
        onPackClicked = { pack ->
            viewModel.setPackSelected(pack.id, !pack.isSelected)
        },
        onEditClicked = { pack ->
            findNavController().navigate(
                R.id.customPackDetailFragment,
                bundleOf("packId" to pack.id)
            )
        }
    )

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

        val locale = resources.configuration.locales[0].language
        viewModel.initialize(locale)
    }

    private fun setupRecyclerView() {
        binding.rvQuestionPacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuestionPacks.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnClosePacks.setOnClickListener {
            findNavController().popBackStack(R.id.playersFragment, false)
        }

        binding.btnCreateCustomPack.setOnClickListener {
            findNavController().navigate(R.id.createCustomPackFragment)
        }

        binding.btnSavePacks.setOnClickListener {
            Timber.i("Selección de packs guardada")
            findNavController().popBackStack(R.id.playersFragment, false)
        }
    }

    private fun observeViewModel() {
        viewModel.packs.observe(viewLifecycleOwner) { packs ->
            adapter.submitList(packs)

            val selectedCount = packs.count { it.isSelected }
            binding.tvSelectedPacksCount.text =
                getString(R.string.packs_selected_count, selectedCount)

            Timber.d("Packs cargados: ${packs.size}, seleccionados=$selectedCount")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("QuestionPacksFragment destruido")
        _binding = null
    }
}