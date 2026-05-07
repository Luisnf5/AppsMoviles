package com.example.tragomaestro.repository

import androidx.lifecycle.LiveData
import com.example.tragomaestro.database.AchievementDao
import com.example.tragomaestro.database.AchievementEntity
import timber.log.Timber

class AchievementRepository(
    private val achievementDao: AchievementDao
) {

    val achievements: LiveData<List<AchievementEntity>> =
        achievementDao.observeAchievements()

    suspend fun initializeAchievementsIfNeeded(locale: String = "es") {
        if (achievementDao.countAchievements() > 0) return

        val isEnglish = locale.startsWith("en")

        val achievements = if (isEnglish) listOf(
            AchievementEntity("first_round", "FIRST DRINK", "Complete your first round.", "beer", 0, 1, false),
            AchievementEntity("party_soul", "PARTY SOUL", "Play with 4 or more players.", "users", 0, 4, false),
            AchievementEntity("full_party", "THIS IS A WEDDING", "Play with 6 or more players.", "users", 0, 6, false),
            AchievementEntity("mind_reader", "HIVE MIND", "The group guesses an answer.", "trophy", 0, 1, false),
            AchievementEntity("telepaths", "BAR TELEPATHS", "The group guesses 3 answers.", "zap", 0, 3, false),
            AchievementEntity("social_disaster", "SOCIAL DISASTER", "The group fails an answer.", "skull", 0, 1, false),
            AchievementEntity("not_even_one", "NOT EVEN ONE", "The group fails 3 answers.", "skull", 0, 3, false),
            AchievementEntity("warming_up", "WARMING UP", "Play 5 rounds.", "fire", 0, 5, false),
            AchievementEntity("out_of_control", "OUT OF CONTROL", "Play 10 rounds.", "fire", 0, 10, false)
        ) else listOf(
            AchievementEntity("first_round", "PRIMER TRAGO", "Completa tu primera ronda.", "beer", 0, 1, false),
            AchievementEntity("party_soul", "ALMA DE LA FIESTA", "Juega con 4 o más jugadores.", "users", 0, 4, false),
            AchievementEntity("full_party", "ESTO ES UNA BODA", "Juega con 6 o más jugadores.", "users", 0, 6, false),
            AchievementEntity("mind_reader", "MENTE COLMENA", "El grupo acierta una respuesta.", "trophy", 0, 1, false),
            AchievementEntity("telepaths", "TELÉPATAS DE BAR", "El grupo acierta 3 respuestas.", "zap", 0, 3, false),
            AchievementEntity("social_disaster", "DESASTRE SOCIAL", "El grupo falla una respuesta.", "skull", 0, 1, false),
            AchievementEntity("not_even_one", "NI UNA", "El grupo falla 3 respuestas.", "skull", 0, 3, false),
            AchievementEntity("warming_up", "CALENTANDO", "Juega 5 rondas.", "fire", 0, 5, false),
            AchievementEntity("out_of_control", "ESTO SE VA DE LAS MANOS", "Juega 10 rondas.", "fire", 0, 10, false)
        )

        achievementDao.insertAll(achievements)
        Timber.i("Logros iniciales insertados")
    }

    suspend fun registerRoundCompleted() {
        achievementDao.incrementProgress("first_round")
        achievementDao.incrementProgress("warming_up")
        achievementDao.incrementProgress("out_of_control")
    }

    suspend fun registerPlayersCount(count: Int) {
        if (count >= 4) achievementDao.unlockAchievement("party_soul")
        if (count >= 6) achievementDao.unlockAchievement("full_party")
    }

    suspend fun registerCorrectGuess() {
        achievementDao.incrementProgress("mind_reader")
        achievementDao.incrementProgress("telepaths")
    }

    suspend fun registerFailedGuess() {
        achievementDao.incrementProgress("social_disaster")
        achievementDao.incrementProgress("not_even_one")
    }
}