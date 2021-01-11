package com.example.toolsdisplay.login.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.toolsdisplay.models.LoginRequest

interface LoginRepository {

    val accessToken: LiveData<String>

     fun attemptLogin(request: LoginRequest)

}