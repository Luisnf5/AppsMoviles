package com.example.tragomaestro.repository

import com.example.tragomaestro.database.AchievementEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser get() = auth.currentUser

    suspend fun signInWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Timber.i("Login con Google exitoso: ${auth.currentUser?.email}")
            true
        } catch (e: Exception) {
            Timber.e(e, "Error en login con Google")
            false
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun syncAchievementsToCloud(achievements: List<AchievementEntity>) {
        val uid = auth.currentUser?.uid ?: return
        try {
            val batch = firestore.batch()
            achievements.forEach { achievement ->
                val ref = firestore
                    .collection("users")
                    .document(uid)
                    .collection("achievements")
                    .document(achievement.id)
                batch.set(ref, mapOf(
                    "id" to achievement.id,
                    "progress" to achievement.progress,
                    "unlocked" to achievement.unlocked
                ))
            }
            batch.commit().await()
            Timber.i("Logros sincronizados en Firestore")
        } catch (e: Exception) {
            Timber.e(e, "Error al sincronizar logros")
        }
    }

    suspend fun loadAchievementsFromCloud(): List<Map<String, Any>> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            firestore.collection("users")
                .document(uid)
                .collection("achievements")
                .get().await()
                .documents
                .mapNotNull { it.data }
        } catch (e: Exception) {
            Timber.e(e, "Error al cargar logros desde Firestore")
            emptyList()
        }
    }
}