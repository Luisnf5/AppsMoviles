package com.example.tragomaestro.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.tragomaestro.database.AchievementEntity
import com.example.tragomaestro.database.TragoMaestroDatabase
import timber.log.Timber

class AchievementRepository(context: Context) {

    private val achievementDao =
        TragoMaestroDatabase.getDatabase(context).achievementDao()

    val achievements: LiveData<List<AchievementEntity>> =
        achievementDao.observeAchievements()

    suspend fun initializeAchievementsIfNeeded() {
        if (achievementDao.countAchievements() > 0) {
            Timber.d("Los logros ya estaban inicializados")
            return
        }

        val defaultAchievements = listOf(
            AchievementEntity(
                id = "first_round",
                title = "PRIMER TRAGO",
                description = "Completa tu primera ronda sin escupir el líquido.",
                iconName = "beer",
                progress = 0,
                target = 1,
                unlocked = false
            ),
            AchievementEntity(
                id = "party_soul",
                title = "ALMA DE LA FIESTA",
                description = "Juega con 4 o más jugadores en una misma partida.",
                iconName = "users",
                progress = 0,
                target = 4,
                unlocked = false
            ),
            AchievementEntity(
                id = "mind_reader",
                title = "MENTE COLMENA",
                description = "El grupo acierta una respuesta del sujeto.",
                iconName = "trophy",
                progress = 0,
                target = 1,
                unlocked = false
            ),
            AchievementEntity(
                id = "social_disaster",
                title = "DESASTRE SOCIAL",
                description = "El grupo falla intentando adivinar al sujeto.",
                iconName = "skull",
                progress = 0,
                target = 1,
                unlocked = false
            ),
            AchievementEntity(
                id = "telepaths",
                title = "TELÉPATAS",
                description = "El grupo acierta 3 respuestas.",
                iconName = "zap",
                progress = 0,
                target = 3,
                unlocked = false
            ),
            AchievementEntity(
                id = "not_even_one",
                title = "NI UNA",
                description = "El grupo falla 3 respuestas. Preocupante.",
                iconName = "skull",
                progress = 0,
                target = 3,
                unlocked = false
            ),
            AchievementEntity(
                id = "warming_up",
                title = "CALENTANDO",
                description = "Juega 5 rondas.",
                iconName = "fire",
                progress = 0,
                target = 5,
                unlocked = false
            ),
            AchievementEntity(
                id = "immortal",
                title = "EL INMORTAL",
                description = "Llega al final de la noche sin llamar a tu ex ni a tu jefe.",
                iconName = "star",
                progress = 0,
                target = 10,
                unlocked = false
            )
        )

        achievementDao.insertAll(defaultAchievements)
        Timber.i("Logros iniciales insertados en Room")
    }

    suspend fun registerRoundCompleted() {
        achievementDao.incrementProgress("first_round")
        achievementDao.incrementProgress("warming_up")
        achievementDao.incrementProgress("immortal")
    }

    suspend fun registerPlayersCount(count: Int) {
        if (count >= 4) {
            achievementDao.unlockAchievement("party_soul")
        }
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