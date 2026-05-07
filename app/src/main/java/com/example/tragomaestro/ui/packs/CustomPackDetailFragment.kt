package com.example.tragomaestro.ui.packs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentCustomPackDetailBinding
import com.example.tragomaestro.repository.QuestionPackRepository
import com.example.tragomaestro.viewmodel.QuestionPacksViewModel
import com.example.tragomaestro.viewmodel.QuestionPacksViewModelFactory
import kotlinx.coroutines.launch
import timber.log.Timber

class CustomPackDetailFragment : Fragment(R.layout.fragment_custom_pack_detail) {

    private var _binding: FragmentCustomPackDetailBinding? = null
    private val binding get() = _binding!!

    private val packId: Int by lazy {
        requireArguments().getInt("packId")
    }

    private val adapter = CustomQuestionsAdapter { question ->
        viewModel.deleteQuestion(question)
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

        _binding = FragmentCustomPackDetailBinding.bind(view)

        setupRecyclerView()
        setupListeners()
        observeQuestions()
        loadPackInfo()
    }

    private fun setupRecyclerView() {
        binding.rvCustomQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCustomQuestions.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnCloseCustomPackDetail.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddCustomQuestion.setOnClickListener {
            findNavController().navigate(
                R.id.addCustomQuestionFragment,
                bundleOf("packId" to packId)
            )
        }

        binding.btnDeleteCustomPack.setOnClickListener {
            lifecycleScope.launch {
                val pack = viewModel.getPackById(packId)
                if (pack != null && pack.isCustom) {
                    viewModel.deletePack(pack)
                    Timber.i("Pack personalizado eliminado desde detalle: ${pack.name}")
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun observeQuestions() {
        viewModel.getQuestionsByPack(packId).observe(viewLifecycleOwner) { questions ->
            adapter.submitList(questions)
            binding.tvCustomQuestionsCount.text =
                getString(R.string.custom_pack_question_count, questions.size)
        }
    }

    private fun loadPackInfo() {
        lifecycleScope.launch {
            val pack = viewModel.getPackById(packId)
            if (pack != null) {
                binding.tvCustomPackTitle.text = pack.name
                binding.tvCustomPackDescription.text = pack.description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}