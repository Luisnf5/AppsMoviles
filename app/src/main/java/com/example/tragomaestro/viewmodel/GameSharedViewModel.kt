package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tragomaestro.model.Player
import timber.log.Timber

class GameSharedViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(emptyList())
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>()
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    fun addPlayer(name: String) {
        val cleanName = name.trim().uppercase()

        if (cleanName.isBlank()) {
            Timber.w("Se intentó añadir un jugador vacío")
            return
        }

        val currentList = _players.value?.toMutableList() ?: mutableListOf()

        if (currentList.any { it.name.equals(cleanName, ignoreCase = true) }) {
            Timber.w("Se intentó añadir un jugador duplicado: $cleanName")
            return
        }

        currentList.add(Player(cleanName))
        _players.value = currentList
        Timber.i("Jugador añadido: $cleanName. Total jugadores: ${currentList.size}")
    }

    fun removePlayer(player: Player) {
        val currentList = _players.value?.toMutableList() ?: mutableListOf()
        currentList.remove(player)
        _players.value = currentList
        Timber.i("Jugador eliminado: ${player.name}. Total jugadores: ${currentList.size}")
    }

    fun canStartGame(): Boolean {
        val canStart = (_players.value?.size ?: 0) >= 2
        Timber.d("Comprobando si se puede iniciar el juego: $canStart")
        return canStart
    }

    fun selectRandomPlayer() {
        val currentList = _players.value ?: emptyList()
        if (currentList.isNotEmpty()) {
            _selectedPlayer.value = currentList.random()
            Timber.i("Jugador seleccionado aleatoriamente: ${_selectedPlayer.value?.name}")
        } else {
            Timber.w("No se pudo seleccionar jugador aleatorio: lista vacía")
        }
    }

    fun clearSelectedPlayer() {
        _selectedPlayer.value = null
        Timber.d("Jugador seleccionado limpiado")
    }
}