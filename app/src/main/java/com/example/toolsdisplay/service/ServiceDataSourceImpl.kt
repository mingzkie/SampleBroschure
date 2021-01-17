package com.example.toolsdisplay.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.network.NoConnectivityException
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.models.ToolsItemListResponse
import java.lang.Exception

class ServiceDataSourceImpl(private val service: Service) : ServiceDataSource {

    private var _accessToken = MutableLiveData<String>()
    override val accessToken: LiveData<String>
        get() = _accessToken

    private var _toolsList = MutableLiveData<List<ToolsItemListResponse.ToolsInfoItem>>()
    override val toolsList: LiveData<List<ToolsItemListResponse.ToolsInfoItem>>
        get() = _toolsList

    private var _productItem = MutableLiveData<ToolsItemListResponse.ToolsInfoItem>()
    override val productItem: LiveData<ToolsItemListResponse.ToolsInfoItem>
        get() = _productItem

    private var _errorMesage = MutableLiveData<ErrorResponseEvent>()
    override val errorMessage: LiveData<ErrorResponseEvent>
        get() = _errorMesage

    override suspend fun login(request: LoginRequest) {
        Log.d("ServiceDataSource", "Invoking log in")
        try {
            val loginInvocation = service.login(request)
            this._accessToken.postValue(loginInvocation)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet")
            _errorMesage.postValue(ErrorResponseEvent.CONNECTION_EROR)
        } catch (e: Exception) {
            Log.e("Generic error", "Generic Error")
            e.printStackTrace()
            _errorMesage.postValue(ErrorResponseEvent.GENERIC_ERROR)
        }
    }

    override suspend fun getToolsList(pageSize: Int, sortOrder: String, field: String, accessToken: String) {
        Log.d("ServiceDataSource", "Invoking list")
        try {
             var fetchedData = service.getList(pageSize, sortOrder, field, accessToken)
             this._toolsList.postValue(fetchedData.items)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet")
            _errorMesage.postValue(ErrorResponseEvent.CONNECTION_EROR)
        } catch (e: Exception) {
            Log.e("Generic error", "Generic Error")
            e.printStackTrace()
            _errorMesage.postValue(ErrorResponseEvent.GENERIC_ERROR)
        }

    }

    override suspend fun getProductItem(sku: String, accessToken: String) {
        Log.d("ServiceDataSource", "Invoking productItem")
        try {
            var fetchedData = service.getProductDetail(sku, accessToken)
            this._productItem.postValue(fetchedData)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet")
            _errorMesage.postValue(ErrorResponseEvent.CONNECTION_EROR)
        } catch (e: Exception) {
            Log.e("Generic error", "Generic Error")
            e.printStackTrace()
            _errorMesage.postValue(ErrorResponseEvent.GENERIC_ERROR)
        }
    }

    enum class ErrorResponseEvent(val errorMessage: String) {
        CONNECTION_EROR("Please check your internet connection"),
        GENERIC_ERROR("Something went wrong. Please try again")
    }

}