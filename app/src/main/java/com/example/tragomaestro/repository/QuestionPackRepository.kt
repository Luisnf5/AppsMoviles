package com.example.tragomaestro.repository

import androidx.lifecycle.LiveData
import com.example.tragomaestro.database.QuestionDao
import com.example.tragomaestro.database.QuestionEntity
import com.example.tragomaestro.database.QuestionPackDao
import com.example.tragomaestro.database.QuestionPackEntity
import com.example.tragomaestro.model.AnswerOption
import com.example.tragomaestro.model.AnswerStyle
import com.example.tragomaestro.model.Question
import timber.log.Timber

class QuestionPackRepository(
    private val packDao: QuestionPackDao,
    private val questionDao: QuestionDao
) {

    val packs: LiveData<List<QuestionPackEntity>> =
        packDao.getAllPacks()

    suspend fun createPack(
        name: String,
        description: String,
        isCustom: Boolean = true
    ): Long {
        return packDao.insertPack(
            QuestionPackEntity(
                name = name,
                description = description,
                isSelected = true,
                isCustom = isCustom
            )
        )
    }

    suspend fun addQuestion(
        packId: Int,
        text: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String
    ) {
        questionDao.insertQuestion(
            QuestionEntity(
                packId = packId,
                text = text,
                optionA = optionA,
                optionB = optionB,
                optionC = optionC,
                optionD = optionD
            )
        )
    }

    fun getQuestionsByPack(packId: Int): LiveData<List<QuestionEntity>> {
        return questionDao.getQuestionsByPack(packId)
    }

    suspend fun setPackSelected(packId: Int, selected: Boolean) {
        packDao.setPackSelected(packId, selected)
    }

    suspend fun getRandomQuestionFromSelectedPacks(): Question? {
        initializeDefaultPacksIfNeeded()

        val selectedQuestions = questionDao.getQuestionsFromSelectedPacks()
        val questions = if (selectedQuestions.isNotEmpty()) {
            selectedQuestions
        } else {
            questionDao.getAllQuestions()
        }

        return questions.randomOrNull()?.toDomain()
    }

    suspend fun deletePack(pack: QuestionPackEntity) {
        packDao.deletePack(pack)
    }

    suspend fun deleteQuestion(question: QuestionEntity) {
        questionDao.deleteQuestion(question)
    }

    suspend fun initializeDefaultPacksIfNeeded() {
        if (packDao.countPacks() > 0) {
            Timber.d("Los packs ya estaban inicializados")
            return
        }

        val classicPackId = createPack(
            name = "Fiesta Clásica",
            description = "Preguntas básicas para empezar suave y perder la vergüenza.",
            isCustom = false
        ).toInt()

        val chaosPackId = createPack(
            name = "Caos Social",
            description = "Preguntas para grupos que ya no tienen nada que ocultar.",
            isCustom = false
        ).toInt()

        val shamePackId = createPack(
            name = "Vergüenza Ajena",
            description = "Historias incómodas, excusas malas y recuerdos que dolían menos olvidados.",
            isCustom = false
        ).toInt()

        val spicyPackId = createPack(
            name = "Picante Controlado",
            description = "Preguntas más atrevidas, pero sin convertir la app en una demanda judicial.",
            isCustom = false
        ).toInt()

        questionDao.insertQuestions(
            listOf(
                QuestionEntity(
                    packId = classicPackId,
                    text = "¿Cuál ha sido el momento más vergonzoso de tu última fiesta?",
                    optionA = "Cantar karaoke fatal",
                    optionB = "Confundirme de persona",
                    optionC = "Caerme bailando",
                    optionD = "Escribirle a mi ex"
                ),
                QuestionEntity(
                    packId = classicPackId,
                    text = "¿Qué excusa usarías para irte antes de una fiesta?",
                    optionA = "Tengo sueño",
                    optionB = "Me duele la cabeza",
                    optionC = "Tengo que madrugar",
                    optionD = "Desaparecer sin decir nada"
                ),
                QuestionEntity(
                    packId = classicPackId,
                    text = "¿Qué harías si suena una canción que odias?",
                    optionA = "Bailarla igual",
                    optionB = "Ir al baño misteriosamente",
                    optionC = "Quejarme como señor mayor",
                    optionD = "Cambiarla sin permiso"
                ),

                QuestionEntity(
                    packId = chaosPackId,
                    text = "¿Qué es lo más raro que has comido o bebido de fiesta?",
                    optionA = "Una mezcla imposible",
                    optionB = "Algo del suelo",
                    optionC = "Un chupito sospechoso",
                    optionD = "No quiero hablar de ello"
                ),
                QuestionEntity(
                    packId = chaosPackId,
                    text = "¿Quién del grupo sobreviviría peor a una noche sin móvil?",
                    optionA = "El más dramático",
                    optionB = "El que siempre se pierde",
                    optionC = "El que pide Uber a 20 metros",
                    optionD = "Yo claramente"
                ),
                QuestionEntity(
                    packId = chaosPackId,
                    text = "¿Qué harías si te despiertas sin recordar cómo llegaste a casa?",
                    optionA = "Mirar Google Maps",
                    optionB = "Preguntar en el grupo",
                    optionC = "Hacer como si nada",
                    optionD = "Culpar al destino"
                ),

                QuestionEntity(
                    packId = shamePackId,
                    text = "¿Qué situación te daría más vergüenza que se contara aquí?",
                    optionA = "Un audio enviado mal",
                    optionB = "Una caída épica",
                    optionC = "Un mensaje desesperado",
                    optionD = "Un baile grabado"
                ),
                QuestionEntity(
                    packId = shamePackId,
                    text = "¿Cuál sería tu peor frase para ligar?",
                    optionA = "¿Estudias o trabajas?",
                    optionB = "Te conozco de algo",
                    optionC = "Mi madre dice que soy majo",
                    optionD = "No suelo hacer esto"
                ),

                QuestionEntity(
                    packId = spicyPackId,
                    text = "¿Qué secreto sería peor que descubriera todo el grupo?",
                    optionA = "Mis búsquedas recientes",
                    optionB = "Mis chats archivados",
                    optionC = "Mis notas del móvil",
                    optionD = "Mi historial de likes"
                ),
                QuestionEntity(
                    packId = spicyPackId,
                    text = "¿Cuál sería tu mayor red flag en una cita?",
                    optionA = "Hablar de mi ex",
                    optionB = "Llegar tarde",
                    optionC = "Mirar mucho el móvil",
                    optionD = "Pedir probar toda la comida"
                )
            )
        )

        Timber.i("Packs y preguntas iniciales insertados en Room")
    }

    private fun QuestionEntity.toDomain(): Question {
        return Question(
            id = id,
            text = text,
            answers = listOf(
                AnswerOption(optionA, AnswerStyle.PINK),
                AnswerOption(optionB, AnswerStyle.ORANGE),
                AnswerOption(optionC, AnswerStyle.BLUE),
                AnswerOption(optionD, AnswerStyle.PURPLE)
            )
        )
    }
}