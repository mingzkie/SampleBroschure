package com.example.toolsdisplay.login.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsdisplay.login.repository.LoginRepository
import com.example.toolsdisplay.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    var accessToken: LiveData<String> = loginRepository.accessToken

    fun attemptLogin(username: String, password: String) {

            Log.d("LoginViewModel", "Invoking list in Login View model ")
            val request = LoginRequest(username, password)
            loginRepository.attemptLogin(request)

    }
}