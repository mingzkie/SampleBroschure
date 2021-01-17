package com.example.toolsdisplay.login.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.service.ServiceDataSourceImpl

interface LoginRepository {

    val accessToken: LiveData<String>

    val errorMessage: LiveData<ServiceDataSourceImpl.ErrorResponseEvent>

     fun attemptLogin(request: LoginRequest)

     suspend fun getAccessToken() : String

}