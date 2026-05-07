package com.example.tragomaestro.ui.achievements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentAchievementsBinding
import com.example.tragomaestro.repository.AchievementRepository
import com.example.tragomaestro.viewmodel.AchievementsViewModel
import com.example.tragomaestro.viewmodel.AchievementsViewModelFactory
import timber.log.Timber

class AchievementsFragment : Fragment(R.layout.fragment_achievements) {

    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    private val adapter = AchievementsAdapter()

    private val viewModel: AchievementsViewModel by viewModels {
        val database = (requireActivity().application as TragoMaestroApplication).database
        val repository = AchievementRepository(database.achievementDao())
        AchievementsViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAchievementsBinding.bind(view)
        Timber.i("AchievementsFragment cargado")

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        val locale = resources.configuration.locales[0].language
        viewModel.initialize(locale)
    }

    private fun setupRecyclerView() {
        binding.rvAchievements.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAchievements.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnCloseAchievements.setOnClickListener {
            findNavController().popBackStack(R.id.playersFragment, false)
        }

        binding.navPlayersFromAchievements.setOnClickListener {
            findNavController().navigate(R.id.playersFragment)
        }

        binding.navRulesFromAchievements.setOnClickListener {
            findNavController().navigate(R.id.rulesFragment)
        }

        binding.navProfileFromAchievements.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
            adapter.submitList(achievements)

            val unlocked = achievements.count { it.unlocked }
            val total = achievements.size

            binding.tvUnlockedCount.text = "$unlocked/$total"
            Timber.d("Logros cargados: $unlocked/$total")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("AchievementsFragment destruido")
        _binding = null
    }
}