package com.example.tragomaestro.ui.account

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.tragomaestro.R
import com.example.tragomaestro.TragoMaestroApplication
import com.example.tragomaestro.databinding.FragmentAccountBinding
import com.example.tragomaestro.viewmodel.AchievementsViewModel
import com.example.tragomaestro.viewmodel.AchievementsViewModelFactory
import com.example.tragomaestro.viewmodel.AuthViewModel
import com.example.tragomaestro.viewmodel.AuthViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import timber.log.Timber

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by lazy {
        val repo = com.example.tragomaestro.repository.UserRepository()
        AuthViewModelFactory(repo).create(AuthViewModel::class.java)
    }

    private val achievementsViewModel: AchievementsViewModel by lazy {
        val app = requireActivity().application as TragoMaestroApplication
        val dao = app.database.achievementDao()
        AchievementsViewModelFactory(com.example.tragomaestro.repository.AchievementRepository(dao))
            .create(AchievementsViewModel::class.java)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                authViewModel.signInWithGoogle(token)
            }
        } catch (e: ApiException) {
            Timber.e(e, "Google Sign-In falló")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnCloseAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignInGoogle.setOnClickListener {
            launchGoogleSignIn()
        }

        binding.btnSignOut.setOnClickListener {
            authViewModel.signOut()
        }

        binding.btnSyncAchievements.setOnClickListener {
            achievementsViewModel.achievements.value?.let { list ->
                authViewModel.syncAchievements(list)
                Timber.i("Sincronizando ${list.size} logros")
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.layoutNotLoggedIn.visibility = View.GONE
                binding.layoutLoggedIn.visibility = View.VISIBLE
                binding.tvUserName.text = user.displayName ?: ""
                binding.tvUserEmail.text = user.email ?: ""
                Glide.with(this)
                    .load(user.photoUrl)
                    .placeholder(R.drawable.ic_user)
                    .into(binding.ivUserAvatar)
            } else {
                binding.layoutNotLoggedIn.visibility = View.VISIBLE
                binding.layoutLoggedIn.visibility = View.GONE
            }
        }
    }

    private fun launchGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInLauncher.launch(client.signInIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}