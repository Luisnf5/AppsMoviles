package com.example.tragomaestro.ui.players

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tragomaestro.R
import com.example.tragomaestro.databinding.FragmentPlayersBinding
import com.example.tragomaestro.model.Player
import com.example.tragomaestro.viewmodel.GameSharedViewModel

class PlayersFragment : Fragment(R.layout.fragment_players) {

    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    private val gameSharedViewModel: GameSharedViewModel by activityViewModels()

    private val chipStyles = listOf(
        R.drawable.bg_player_chip_pink,
        R.drawable.bg_player_chip_blue,
        R.drawable.bg_player_chip_orange,
        R.drawable.bg_player_chip_green,
        R.drawable.bg_player_chip_purple
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPlayersBinding.bind(view)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddPlayer.setOnClickListener {
            addPlayerFromInput()
        }

        binding.etNewPlayer.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addPlayerFromInput()
                true
            } else {
                false
            }
        }

        binding.btnEditPacks.setOnClickListener {
            // Actívalo cuando exista packsFragment
            // findNavController().navigate(R.id.packsFragment)
        }

        binding.btnStartGame.setOnClickListener {
            if (gameSharedViewModel.canStartGame()) {
                gameSharedViewModel.selectRandomPlayer()
                // Actívalo cuando exista turnFragment
                // findNavController().navigate(R.id.turnFragment)
            }
        }

        binding.navRules.setOnClickListener {
            findNavController().navigate(R.id.rulesFragment)
        }

        binding.navAchievements.setOnClickListener {
            // Actívalo cuando exista achievementsFragment
            // findNavController().navigate(R.id.achievementsFragment)
        }
    }

    private fun observeViewModel() {
        gameSharedViewModel.players.observe(viewLifecycleOwner) { players ->
            renderPlayers(players)

            val canStart = players.size >= 2
            binding.btnStartGame.isEnabled = canStart
            binding.btnStartGame.alpha = if (canStart) 1.0f else 0.45f
        }
    }

    private fun addPlayerFromInput() {
        val newPlayer = binding.etNewPlayer.text.toString()
        gameSharedViewModel.addPlayer(newPlayer)
        binding.etNewPlayer.setText("")
    }

    private fun renderPlayers(players: List<Player>) {
        binding.layoutPlayersChips.removeAllViews()

        players.forEachIndexed { index, player ->
            binding.layoutPlayersChips.addView(createPlayerChip(player, index))
        }
    }

    private fun createPlayerChip(player: Player, index: Int): View {
        val context = requireContext()

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = context.getDrawable(chipStyles[index % chipStyles.size])
            setPadding(dp(20), dp(10), dp(16), dp(10))
        }

        val params = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            rightMargin = dp(8)
            bottomMargin = dp(8)
        }
        container.layoutParams = params

        val nameView = TextView(context).apply {
            text = player.name
            setTextColor(resources.getColor(android.R.color.white, null))
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
        }

        val closeView = ImageView(context).apply {
            setImageResource(R.drawable.ic_close)
            imageTintList = ColorStateList.valueOf(
                resources.getColor(android.R.color.white, null)
            )
            alpha = 0.6f
            layoutParams = LinearLayout.LayoutParams(dp(14), dp(14)).apply {
                leftMargin = dp(8)
            }
            setOnClickListener {
                gameSharedViewModel.removePlayer(player)
            }
        }

        container.addView(nameView)
        container.addView(closeView)

        return container
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}