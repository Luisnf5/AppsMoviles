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

    suspend fun initializeDefaultPacksIfNeeded(locale: String = "es") {
        if (packDao.countPacks() > 0) {
            Timber.d("Los packs ya estaban inicializados")
            return
        }

        //la mejor manera de traducir para este caso que vienen hardcodeadas
        val isEnglish = locale.startsWith("en")

        val classicPackId = createPack(
            name = if (isEnglish) "Classic Party" else "Fiesta Clásica",
            description = if (isEnglish) "Basic questions to start easy and lose your shame." else "Preguntas básicas para empezar suave y perder la vergüenza.",
            isCustom = false
        ).toInt()

        val chaosPackId = createPack(
            name = if (isEnglish) "Social Chaos" else "Caos Social",
            description = if (isEnglish) "Questions for groups with nothing left to hide." else "Preguntas para grupos que ya no tienen nada que ocultar.",
            isCustom = false
        ).toInt()

        val shamePackId = createPack(
            name = if (isEnglish) "Second-Hand Embarrassment" else "Vergüenza Ajena",
            description = if (isEnglish) "Uncomfortable stories and bad excuses best forgotten." else "Historias incómodas, excusas malas y recuerdos que dolían menos olvidados.",
            isCustom = false
        ).toInt()

        val spicyPackId = createPack(
            name = if (isEnglish) "Controlled Spicy" else "Picante Controlado",
            description = if (isEnglish) "Bolder questions, but not enough to end up in court." else "Preguntas más atrevidas, pero sin convertir la app en una demanda judicial.",
            isCustom = false
        ).toInt()

        if (isEnglish) {
            questionDao.insertQuestions(
                listOf(
                    QuestionEntity(
                        packId = classicPackId,
                        text = "What was your most embarrassing moment at your last party?",
                        optionA = "Singing karaoke horribly",
                        optionB = "Mistaking someone for another person",
                        optionC = "Falling while dancing",
                        optionD = "Texting my ex"
                    ),
                    QuestionEntity(
                        packId = classicPackId,
                        text = "What excuse would you use to leave a party early?",
                        optionA = "I'm tired",
                        optionB = "I have a headache",
                        optionC = "I have to wake up early",
                        optionD = "Just disappear without saying anything"
                    ),
                    QuestionEntity(
                        packId = classicPackId,
                        text = "What would you do if a song you hate comes on?",
                        optionA = "Dance to it anyway",
                        optionB = "Mysteriously go to the bathroom",
                        optionC = "Complain like an old man",
                        optionD = "Change it without asking"
                    ),

                    QuestionEntity(
                        packId = chaosPackId,
                        text = "What's the weirdest thing you've eaten or drunk at a party?",
                        optionA = "An impossible mix",
                        optionB = "Something off the floor",
                        optionC = "A suspicious shot",
                        optionD = "I don't want to talk about it"
                    ),
                    QuestionEntity(
                        packId = chaosPackId,
                        text = "Who in the group would survive worst without their phone for a night?",
                        optionA = "The most dramatic one",
                        optionB = "The one who always gets lost",
                        optionC = "The one who orders Uber 20 meters away",
                        optionD = "Clearly me"
                    ),
                    QuestionEntity(
                        packId = chaosPackId,
                        text = "What would you do if you wake up not remembering how you got home?",
                        optionA = "Check Google Maps",
                        optionB = "Ask in the group chat",
                        optionC = "Act like nothing happened",
                        optionD = "Blame fate"
                    ),

                    QuestionEntity(
                        packId = shamePackId,
                        text = "What situation would embarrass you most if told here?",
                        optionA = "A voice note sent to the wrong person",
                        optionB = "An epic fall",
                        optionC = "A desperate message",
                        optionD = "A recorded dance"
                    ),
                    QuestionEntity(
                        packId = shamePackId,
                        text = "What would be your worst pickup line?",
                        optionA = "Do you study or work?",
                        optionB = "I feel like I know you",
                        optionC = "My mom says I'm nice",
                        optionD = "I don't usually do this"
                    ),

                    QuestionEntity(
                        packId = spicyPackId,
                        text = "What secret would be worst for the whole group to discover?",
                        optionA = "My recent searches",
                        optionB = "My archived chats",
                        optionC = "My phone notes",
                        optionD = "My likes history"
                    ),
                    QuestionEntity(
                        packId = spicyPackId,
                        text = "What would be your biggest red flag on a date?",
                        optionA = "Talking about my ex",
                        optionB = "Being late",
                        optionC = "Looking at my phone too much",
                        optionD = "Asking to try everything on the menu"
                    )
                )
            )
        } else {
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
        }

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