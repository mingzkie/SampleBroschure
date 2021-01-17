package com.example.toolsdisplay.home.repository

import androidx.lifecycle.LiveData
import com.example.toolsdisplay.database.entities.ToolsInfoData
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.service.ServiceDataSourceImpl

interface ToolsInfoRepository {

    val result: LiveData<List<ToolsInfoDto>>

    val productItem: LiveData<ToolsInfoDto>

    val errorMessage: LiveData<ServiceDataSourceImpl.ErrorResponseEvent>

    suspend fun getToolsInfoList(pageSize: Int, sortOrder: String, field: String)

    suspend fun getProductItem(id: Int, sku: String)

    fun update(toolsInfoData: ToolsInfoData)

    suspend fun reloadData()

}