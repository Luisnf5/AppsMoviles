package com.example.tragomaestro.ui.passtogroup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentPassToGroupBinding
import com.example.tragomaestro.viewmodel.GameSharedViewModel
import timber.log.Timber

class PassToGroupFragment : Fragment(R.layout.fragment_pass_to_group) {

    private var _binding: FragmentPassToGroupBinding? = null
    private val binding get() = _binding!!

    private val gameSharedViewModel: GameSharedViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPassToGroupBinding.bind(view)
        Timber.i("PassToGroupFragment cargado")

        gameSharedViewModel.selectedPlayer.observe(viewLifecycleOwner) { player ->
            val playerName = player?.name ?: "EL SUJETO"
            binding.tvPassToGroupSubject.text = getString(
                R.string.pass_to_group_subject,
                playerName
            )
        }

        binding.btnContinueToGroup.setOnClickListener {
            Timber.i("El móvil pasa al grupo")
            gameSharedViewModel.clearGroupAnswer()
            findNavController().navigate(R.id.groupDecisionFragment)
        }

        binding.btnClosePassToGroup.setOnClickListener {
            Timber.i("Saliendo desde PassToGroupFragment")
            findNavController().navigate(R.id.playersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("PassToGroupFragment destruido")
        _binding = null
    }
}