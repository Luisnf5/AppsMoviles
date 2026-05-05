package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tragomaestro.model.AnswerOption
import com.example.tragomaestro.model.AnswerStyle
import com.example.tragomaestro.model.Player
import com.example.tragomaestro.model.Question
import timber.log.Timber
import com.example.tragomaestro.model.RoundResult

class GameSharedViewModel : ViewModel() {

    private val _players = MutableLiveData<List<Player>>(emptyList())
    val players: LiveData<List<Player>> = _players

    private val _selectedPlayer = MutableLiveData<Player?>()
    val selectedPlayer: LiveData<Player?> = _selectedPlayer

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> = _currentQuestion

    private val _selectedAnswerIndex = MutableLiveData<Int?>(null)
    val selectedAnswerIndex: LiveData<Int?> = _selectedAnswerIndex

    private val _groupAnswerIndex = MutableLiveData<Int?>(null)
    val groupAnswerIndex: LiveData<Int?> = _groupAnswerIndex

    private val _roundResult = MutableLiveData<RoundResult?>()
    val roundResult: LiveData<RoundResult?> = _roundResult

    fun generateRoundResult() {
        val subjectAnswer = _selectedAnswerIndex.value
        val groupAnswer = _groupAnswerIndex.value

        if (subjectAnswer == null || groupAnswer == null) {
            Timber.w("No se puede generar resultado: falta alguna respuesta")
            _roundResult.value = null
            return
        }

        val isCorrect = subjectAnswer == groupAnswer
        val drinkCount = (1..5).random()

        val message = if (isCorrect) {
            listOf(
                "El sujeto no sabe mentir. ¡Que beba por no saber ocultar sus secretos!",
                "Habéis leído su mente como auténticos maestros del trago.",
                "Demasiado fácil... el sujeto era un libro abierto.",
                "El grupo ha olido la verdad desde lejos."
            ).random()
        } else {
            listOf(
                "No conocéis a vuestros amigos ni de lejos. ¡Bebed todos menos el sujeto!",
                "El sujeto os ha engañado como quería. Castigo grupal.",
                "Vaya lectura más mala. El grupo necesita entrenar.",
                "Habéis fallado estrepitosamente. Brindis de la vergüenza."
            ).random()
        }

        _roundResult.value = RoundResult(
            isCorrect = isCorrect,
            drinkCount = drinkCount,
            message = message
        )

        Timber.i("Resultado generado. Acierto=$isCorrect, tragos=$drinkCount")
    }

    fun prepareNextRound() {
        selectRandomPlayer()
        loadRandomQuestion()
        clearSelectedAnswer()
        clearGroupAnswer()
        Timber.i("Nueva ronda preparada")
    }

    fun selectGroupAnswer(index: Int) {
        _groupAnswerIndex.value = index
        Timber.d("Respuesta del grupo seleccionada: índice=$index")
    }

    fun clearGroupAnswer() {
        _groupAnswerIndex.value = null
    }
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
        _groupAnswerIndex.value = null
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