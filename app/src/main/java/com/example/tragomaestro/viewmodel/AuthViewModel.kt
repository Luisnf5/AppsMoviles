package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tragomaestro.database.AchievementEntity
import com.example.tragomaestro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>(userRepository.currentUser)
    val user: LiveData<FirebaseUser?> = _user

    private val _loginError = MutableLiveData<Boolean>(false)
    val loginError: LiveData<Boolean> = _loginError

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val success = userRepository.signInWithGoogle(idToken)
            if (success) {
                _user.value = userRepository.currentUser
                _loginError.value = false
            } else {
                _loginError.value = true
            }
        }
    }

    fun signOut() {
        userRepository.signOut()
        _user.value = null
        Timber.i("Usuario cerró sesión")
    }

    fun syncAchievements(achievements: List<AchievementEntity>) {
        viewModelScope.launch {
            userRepository.syncAchievementsToCloud(achievements)
        }
    }
}