package com.example.toolsdisplay.login.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsdisplay.login.repository.LoginRepository
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.service.ServiceDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    var accessToken: LiveData<String> = loginRepository.accessToken

    var errorEvent: LiveData<ServiceDataSourceImpl.ErrorResponseEvent> = loginRepository.errorMessage

    fun attemptLogin(username: String, password: String) {

            Log.d("LoginViewModel", "Invoking list in Login View model ")
            val request = LoginRequest(username, password)
            loginRepository.attemptLogin(request)

    }

    suspend fun getAccessToken() : String {
        return loginRepository.getAccessToken()
    }
}