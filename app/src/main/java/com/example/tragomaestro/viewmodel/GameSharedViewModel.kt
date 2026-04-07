package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tragomaestro.model.Player

class GameSharedViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(emptyList())
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>()
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    fun addPlayer(name: String) {
        val cleanName = name.trim().uppercase()
        if (cleanName.isBlank()) return

        val currentList = _players.value?.toMutableList() ?: mutableListOf()

        if (currentList.any { it.name.equals(cleanName, ignoreCase = true) }) return

        currentList.add(Player(cleanName))
        _players.value = currentList
    }

    fun removePlayer(player: Player) {
        val currentList = _players.value?.toMutableList() ?: mutableListOf()
        currentList.remove(player)
        _players.value = currentList
    }

    fun canStartGame(): Boolean {
        return (_players.value?.size ?: 0) >= 2
    }

    fun selectRandomPlayer() {
        val currentList = _players.value ?: emptyList()
        if (currentList.isNotEmpty()) {
            _selectedPlayer.value = currentList.random()
        }
    }

    fun clearSelectedPlayer() {
        _selectedPlayer.value = null
    }
}