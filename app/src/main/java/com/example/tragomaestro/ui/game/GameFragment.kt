package com.example.tragomaestro.ui.game

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentGameBinding
import com.example.tragomaestro.model.AnswerOption
import com.example.tragomaestro.model.AnswerStyle
import com.example.tragomaestro.viewmodel.GameSharedViewModel
import timber.log.Timber

class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val gameSharedViewModel: GameSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGameBinding.bind(view)

        setupListeners()
        observeViewModel()

        if (gameSharedViewModel.currentQuestion.value == null) {
            gameSharedViewModel.loadRandomQuestion()
        }

        Timber.i("GameFragment cargado")
    }

    private fun setupListeners() {
        binding.btnCloseGame.setOnClickListener {
            Timber.d("Cerrar juego y volver a players")
            findNavController().navigate(R.id.playersFragment)
        }

        binding.btnConfirmTruth.setOnClickListener {
            val selectedIndex = gameSharedViewModel.selectedAnswerIndex.value
            if (selectedIndex != null) {
                Timber.i("Respuesta confirmada. Índice: $selectedIndex")
                // Aquí irá groupDecisionFragment cuando exista
                // findNavController().navigate(R.id.groupDecisionFragment)
            }
        }

        binding.btnSkipQuestion.setOnClickListener {
            Timber.i("Pregunta omitida")
            gameSharedViewModel.loadRandomQuestion()
        }
    }

    private fun observeViewModel() {
        gameSharedViewModel.selectedPlayer.observe(viewLifecycleOwner) { player ->
            val playerName = player?.name ?: "JUGADOR"
            binding.tvCurrentPlayerBadge.text = "ESTÁS JUGANDO: $playerName"
            Timber.d("Jugador actual mostrado: $playerName")
        }

        gameSharedViewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            if (question != null) {
                binding.tvQuestionText.text = question.text
                renderAnswers(question.answers)
                Timber.d("Pregunta renderizada: ${question.text}")
            }
        }

        gameSharedViewModel.selectedAnswerIndex.observe(viewLifecycleOwner) { selectedIndex ->
            updateConfirmButtonState(selectedIndex != null)
            refreshAnswersSelection()
            Timber.d("Estado botón confirmar actualizado")
        }
    }

    private fun renderAnswers(answers: List<AnswerOption>) {
        binding.layoutAnswersContainer.removeAllViews()

        answers.forEachIndexed { index, answer ->
            binding.layoutAnswersContainer.addView(createAnswerView(answer, index))
        }
    }

    private fun refreshAnswersSelection() {
        val question = gameSharedViewModel.currentQuestion.value ?: return
        renderAnswers(question.answers)
    }

    private fun createAnswerView(answer: AnswerOption, index: Int): View {
        val context = requireContext()
        val selectedIndex = gameSharedViewModel.selectedAnswerIndex.value
        val isSelected = selectedIndex == index

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = context.getDrawable(getAnswerBackground(answer.style, isSelected))
            setPadding(dp(18), dp(18), dp(18), dp(18))
            setOnClickListener {
                gameSharedViewModel.selectAnswer(index)
            }
        }

        val params = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dp(14)
        }
        container.layoutParams = params

        val circle = TextView(context).apply {
            text = ('A' + index).toString()
            gravity = Gravity.CENTER
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(dp(34), dp(34))
            background = context.getDrawable(R.drawable.bg_game_answer_letter_circle)

            if (isSelected) {
                setTextColor(resources.getColor(android.R.color.white, null))
                backgroundTintList = ColorStateList.valueOf(
                    resources.getColor(android.R.color.white, null)
                )
                alpha = 0.18f
            } else {
                setTextColor(getAnswerColor(answer.style))
                alpha = 1f
            }
        }

        val textView = TextView(context).apply {
            text = answer.text
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(resources.getColor(android.R.color.white, null))
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                leftMargin = dp(14)
            }
        }

        container.addView(circle)
        container.addView(textView)

        return container
    }

    private fun getAnswerBackground(style: AnswerStyle, selected: Boolean): Int {
        return when (style) {
            AnswerStyle.PINK -> if (selected) R.drawable.bg_game_answer_pink_selected else R.drawable.bg_game_answer_pink
            AnswerStyle.ORANGE -> if (selected) R.drawable.bg_game_answer_orange_selected else R.drawable.bg_game_answer_orange
            AnswerStyle.BLUE -> if (selected) R.drawable.bg_game_answer_blue_selected else R.drawable.bg_game_answer_blue
            AnswerStyle.PURPLE -> if (selected) R.drawable.bg_game_answer_purple_selected else R.drawable.bg_game_answer_purple
        }
    }

    private fun getAnswerColor(style: AnswerStyle): Int {
        return when (style) {
            AnswerStyle.PINK -> resources.getColor(R.color.tm_pink, null)
            AnswerStyle.ORANGE -> resources.getColor(R.color.tm_orange, null)
            AnswerStyle.BLUE -> resources.getColor(R.color.tm_blue, null)
            AnswerStyle.PURPLE -> resources.getColor(R.color.tm_purple, null)
        }
    }

    private fun updateConfirmButtonState(enabled: Boolean) {
        binding.btnConfirmTruth.isEnabled = enabled
        binding.btnConfirmTruth.alpha = if (enabled) 1f else 0.45f
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("GameFragment destruido")
        _binding = null
    }
}