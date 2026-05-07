package com.example.tragomaestro.ui.packs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentAddCustomQuestionBinding
import com.example.tragomaestro.repository.QuestionPackRepository
import com.example.tragomaestro.viewmodel.QuestionPacksViewModel
import com.example.tragomaestro.viewmodel.QuestionPacksViewModelFactory

class AddCustomQuestionFragment : Fragment(R.layout.fragment_add_custom_question) {

    private var _binding: FragmentAddCustomQuestionBinding? = null
    private val binding get() = _binding!!

    private val packId: Int by lazy {
        requireArguments().getInt("packId")
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

        _binding = FragmentAddCustomQuestionBinding.bind(view)

        binding.btnCloseAddQuestion.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveCustomQuestion.setOnClickListener {
            val question = binding.etQuestionText.text.toString().trim()
            val a = binding.etOptionA.text.toString().trim()
            val b = binding.etOptionB.text.toString().trim()
            val c = binding.etOptionC.text.toString().trim()
            val d = binding.etOptionD.text.toString().trim()

            if (question.isBlank() || a.isBlank() || b.isBlank() || c.isBlank() || d.isBlank()) {
                Toast.makeText(requireContext(), getString(R.string.packs_error_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addQuestion(packId, question, a, b, c, d)
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}