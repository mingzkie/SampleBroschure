package com.example.toolsdisplay.login.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.toolsdisplay.database.ToolsInfoDao
import com.example.toolsdisplay.database.entities.AuthInfoData
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.service.ServiceDataSource
import kotlinx.coroutines.*

class LoginRepositoryImpl(private val toolsInfoDao: ToolsInfoDao, private val serviceDataSource: ServiceDataSource) : LoginRepository {

    var _accessToken = MutableLiveData<String>()
    override val accessToken: LiveData<String>
        get() = _accessToken

    override fun attemptLogin(request: LoginRequest) {
        GlobalScope.launch(Dispatchers.IO ) {
            serviceDataSource.login(request)
        }

        GlobalScope.launch(Dispatchers.Main) {
            serviceDataSource.accessToken.observeForever { newAuthToken ->
                _accessToken.postValue(newAuthToken)
                persistAuthToken(newAuthToken)
            }
        }
    }

    fun persistAuthToken(authToken: String) {
        GlobalScope.launch(Dispatchers.IO) {
            toolsInfoDao.insertAuthToken(AuthInfoData(0, authToken))
        }
    }


}