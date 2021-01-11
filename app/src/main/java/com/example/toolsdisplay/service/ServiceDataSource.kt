package com.example.toolsdisplay.service

import androidx.lifecycle.LiveData
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.models.LoginRequest
import com.example.toolsdisplay.models.ToolsItemListResponse

interface ServiceDataSource {

    val accessToken: LiveData<String>

    val toolsList: LiveData<List<ToolsItemListResponse.ToolsInfoItem>>

    suspend fun login(request: LoginRequest)

    //field value is entity_id
    suspend fun getToolsList(pageSize: Int, sortOrder: String, field: String, accessToken: String)

}