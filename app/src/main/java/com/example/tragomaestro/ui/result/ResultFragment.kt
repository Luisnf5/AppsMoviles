package com.example.tragomaestro.ui.result

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentResultBinding
import com.example.tragomaestro.model.RoundResult
import com.example.tragomaestro.viewmodel.GameSharedViewModel
import timber.log.Timber

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val gameSharedViewModel: GameSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResultBinding.bind(view)
        Timber.i("ResultFragment cargado")

        if (gameSharedViewModel.roundResult.value == null) {
            gameSharedViewModel.generateRoundResult()
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnCloseResult.setOnClickListener {
            Timber.i("Cerrando resultado y volviendo a jugadores")
            findNavController().navigate(R.id.playersFragment)
        }

        binding.btnNextRound.setOnClickListener {
            Timber.i("Preparando siguiente ronda")
            gameSharedViewModel.prepareNextRound()
            findNavController().navigate(R.id.turnFragment)
        }
    }

    private fun observeViewModel() {
        gameSharedViewModel.roundResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                renderResult(result)
            }
        }
    }

    private fun renderResult(result: RoundResult) {
        if (result.isCorrect) {
            renderSuccess(result)
        } else {
            renderFailure(result)
        }

        renderBeerIcons(result.drinkCount)
        binding.tvResultMessage.text = "\"${result.message}\""
    }

    private fun renderSuccess(result: RoundResult) {
        binding.layoutResultIconCircle.setBackgroundResource(R.drawable.bg_result_success_circle)
        binding.layoutResultCard.setBackgroundResource(R.drawable.bg_result_success_card)

        binding.ivResultIcon.setImageResource(R.drawable.ic_zap)
        binding.ivResultIcon.imageTintList = ColorStateList.valueOf(
            resources.getColor(R.color.tm_success, null)
        )

        binding.tvResultTitle.text = getString(R.string.result_success_title)
        binding.tvResultTitle.setTextColor(resources.getColor(R.color.tm_success, null))

        binding.tvResultSubtitle.text = getString(R.string.result_success_subtitle)
        binding.btnNextRound.text = getString(R.string.result_next_success)

        Timber.i("Resultado mostrado: acierto con ${result.drinkCount} tragos")
    }

    private fun renderFailure(result: RoundResult) {
        binding.layoutResultIconCircle.setBackgroundResource(R.drawable.bg_result_failure_circle)
        binding.layoutResultCard.setBackgroundResource(R.drawable.bg_result_failure_card)

        binding.ivResultIcon.setImageResource(R.drawable.ic_skull)
        binding.ivResultIcon.imageTintList = ColorStateList.valueOf(
            resources.getColor(R.color.tm_danger, null)
        )

        binding.tvResultTitle.text = getString(R.string.result_failure_title)
        binding.tvResultTitle.setTextColor(resources.getColor(R.color.tm_danger, null))

        binding.tvResultSubtitle.text = getString(R.string.result_failure_subtitle)
        binding.btnNextRound.text = getString(R.string.result_next_failure)

        Timber.i("Resultado mostrado: fallo con ${result.drinkCount} tragos")
    }

    private fun renderBeerIcons(count: Int) {
        binding.layoutBeerIcons.removeAllViews()

        repeat(count) {
            val beerIcon = ImageView(requireContext()).apply {
                setImageResource(R.drawable.ic_beer)
                imageTintList = ColorStateList.valueOf(
                    resources.getColor(R.color.tm_beer_muted, null)
                )
                layoutParams = LinearLayout.LayoutParams(dp(30), dp(30)).apply {
                    marginStart = dp(6)
                    marginEnd = dp(6)
                }
            }

            binding.layoutBeerIcons.addView(beerIcon)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("ResultFragment destruido")
        _binding = null
    }
}