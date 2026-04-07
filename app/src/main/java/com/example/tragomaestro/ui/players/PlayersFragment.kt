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
import timber.log.Timber

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

        Timber.i("PlayersFragment cargado")
        _binding = FragmentPlayersBinding.bind(view)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener {
            Timber.i("Botón cerrar pulsado en PlayersFragment")
            findNavController().navigateUp()
        }

        binding.btnAddPlayer.setOnClickListener {
            Timber.i("Botón añadir jugador pulsado")
            addPlayerFromInput()
        }

        binding.etNewPlayer.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Timber.i("Enter pulsado en campo de jugador")
                addPlayerFromInput()
                true
            } else {
                false
            }
        }

        binding.btnEditPacks.setOnClickListener {
            Timber.i("Botón editar packs pulsado")
            // Actívalo cuando exista packsFragment
            // findNavController().navigate(R.id.packsFragment)
        }

        binding.btnStartGame.setOnClickListener {
            Timber.i("Botón comenzar juego pulsado")
            if (gameSharedViewModel.canStartGame()) {
                Timber.i("Hay suficientes jugadores, seleccionando jugador aleatorio")
                gameSharedViewModel.selectRandomPlayer()
                // Actívalo cuando exista turnFragment
                // findNavController().navigate(R.id.turnFragment)
            } else {
                Timber.w("No se puede comenzar el juego: jugadores insuficientes")
            }
        }

        binding.navRules.setOnClickListener {
            Timber.i("Navegando a RulesFragment desde PlayersFragment")
            findNavController().navigate(R.id.rulesFragment)
        }

        binding.navAchievements.setOnClickListener {
            Timber.i("Botón logros pulsado")
            // Actívalo cuando exista achievementsFragment
            // findNavController().navigate(R.id.achievementsFragment)
        }
    }

    private fun observeViewModel() {
        gameSharedViewModel.players.observe(viewLifecycleOwner) { players ->
            Timber.d("Lista de jugadores actualizada. Total: ${players.size}")
            renderPlayers(players)

            val canStart = players.size >= 2
            binding.btnStartGame.isEnabled = canStart
            binding.btnStartGame.alpha = if (canStart) 1.0f else 0.45f

            Timber.d("Estado del botón comenzar juego. Habilitado: $canStart")
        }
    }

    private fun addPlayerFromInput() {
        val newPlayer = binding.etNewPlayer.text.toString()
        Timber.d("Intentando añadir jugador desde input: '$newPlayer'")
        gameSharedViewModel.addPlayer(newPlayer)
        binding.etNewPlayer.setText("")
    }

    private fun renderPlayers(players: List<Player>) {
        Timber.d("Renderizando chips de jugadores")
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
                Timber.i("Eliminando jugador desde chip: ${player.name}")
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
        Timber.i("Destruyendo vista de PlayersFragment")
        super.onDestroyView()
        _binding = null
    }
}