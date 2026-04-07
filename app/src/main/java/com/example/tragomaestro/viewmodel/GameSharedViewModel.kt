package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tragomaestro.model.AnswerOption
import com.example.tragomaestro.model.AnswerStyle
import com.example.tragomaestro.model.Player
import com.example.tragomaestro.model.Question
import timber.log.Timber

class GameSharedViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(emptyList())
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>()
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> = _currentQuestion

    private val _selectedAnswerIndex = MutableLiveData<Int?>(null)
    val selectedAnswerIndex: LiveData<Int?> = _selectedAnswerIndex

    private val provisionalQuestions = listOf(
        Question(
            id = 1,
            text = "¿Cuál ha sido el momento más vergonzoso de tu última fiesta?",
            answers = listOf(
                AnswerOption("Cantar karaoke fatal", AnswerStyle.PINK),
                AnswerOption("Confundirme de persona", AnswerStyle.ORANGE),
                AnswerOption("Caerme bailando", AnswerStyle.BLUE),
                AnswerOption("Escribirle a mi ex", AnswerStyle.PURPLE)
            )
        ),
        Question(
            id = 2,
            text = "¿Cuál ha sido la mayor locura que has hecho en una noche de fiesta?",
            answers = listOf(
                AnswerOption("Perder el móvil", AnswerStyle.PINK),
                AnswerOption("Dormirme en un sofá ajeno", AnswerStyle.ORANGE),
                AnswerOption("Salir sin cartera", AnswerStyle.BLUE),
                AnswerOption("Llamar a mi ex", AnswerStyle.PURPLE)
            )
        ),
        Question(
            id = 3,
            text = "¿Qué es lo más raro que has comido o bebido de fiesta?",
            answers = listOf(
                AnswerOption("Una mezcla imposible", AnswerStyle.PINK),
                AnswerOption("Algo del suelo", AnswerStyle.ORANGE),
                AnswerOption("Un chupito sospechoso", AnswerStyle.BLUE),
                AnswerOption("No quiero hablar de ello", AnswerStyle.PURPLE)
            )
        )
    )

    fun addPlayer(name: String) {
        val cleanName = name.trim().uppercase()
        if (cleanName.isBlank()) {
            Timber.d("No se añade jugador: nombre vacío")
            return
        }

        val currentList = _players.value?.toMutableList() ?: mutableListOf()

        if (currentList.any { it.name.equals(cleanName, ignoreCase = true) }) {
            Timber.d("No se añade jugador duplicado: $cleanName")
            return
        }

        currentList.add(Player(cleanName))
        _players.value = currentList
        Timber.i("Jugador añadido: $cleanName")
    }

    fun removePlayer(player: Player) {
        val currentList = _players.value?.toMutableList() ?: mutableListOf()
        currentList.remove(player)
        _players.value = currentList
        Timber.i("Jugador eliminado: ${player.name}")
    }

    fun canStartGame(): Boolean {
        return (_players.value?.size ?: 0) >= 2
    }

    fun selectRandomPlayer() {
        val currentList = _players.value ?: emptyList()
        if (currentList.isNotEmpty()) {
            val randomPlayer = currentList.random()
            _selectedPlayer.value = randomPlayer
            Timber.i("Jugador seleccionado aleatoriamente: ${randomPlayer.name}")
        } else {
            Timber.w("No hay jugadores para seleccionar")
        }
    }

    fun clearSelectedPlayer() {
        _selectedPlayer.value = null
        Timber.d("Jugador seleccionado limpiado")
    }

    fun loadRandomQuestion() {
        if (provisionalQuestions.isEmpty()) {
            Timber.w("No hay preguntas disponibles")
            _currentQuestion.value = null
            return
        }

        val question = provisionalQuestions.random()
        _currentQuestion.value = question
        _selectedAnswerIndex.value = null
        Timber.i("Pregunta cargada: ${question.id}")
    }

    fun selectAnswer(index: Int) {
        _selectedAnswerIndex.value = index
        Timber.d("Respuesta seleccionada: índice=$index")
    }

    fun clearSelectedAnswer() {
        _selectedAnswerIndex.value = null
        Timber.d("Respuesta seleccionada limpiada")
    }
}