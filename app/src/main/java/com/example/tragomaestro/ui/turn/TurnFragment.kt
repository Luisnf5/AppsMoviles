package com.example.tragomaestro.ui.turn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentTurnBinding
import com.example.tragomaestro.viewmodel.GameSharedViewModel
import timber.log.Timber

class TurnFragment : Fragment(R.layout.fragment_turn) {

    private var _binding: FragmentTurnBinding? = null
    private val binding get() = _binding!!

    private val gameSharedViewModel: GameSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i("TurnFragment cargado")
        _binding = FragmentTurnBinding.bind(view)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnCloseTurn.setOnClickListener {
            Timber.i("Botón cerrar pulsado en TurnFragment")
            findNavController().navigateUp()
        }

        binding.btnReady.setOnClickListener {
            Timber.i("Botón ¡SOY YO! pulsado")
            findNavController().navigate(R.id.gameFragment)
        }
    }

    private fun observeViewModel() {
        gameSharedViewModel.selectedPlayer.observe(viewLifecycleOwner) { player ->
            if (player == null) {
                Timber.w("TurnFragment abierto sin jugador seleccionado")
                binding.tvCurrentPlayer.text = getString(R.string.turn_no_player)
                binding.tvPassDevice.text = getString(R.string.turn_no_player_desc)
            } else {
                Timber.i("Mostrando turno del jugador: ${player.name}")
                binding.tvCurrentPlayer.text = player.name
                binding.tvPassDevice.text = getString(R.string.turn_pass_device_dynamic, player.name)
            }
        }
    }

    override fun onDestroyView() {
        Timber.i("Destruyendo vista de TurnFragment")
        super.onDestroyView()
        _binding = null
    }
}