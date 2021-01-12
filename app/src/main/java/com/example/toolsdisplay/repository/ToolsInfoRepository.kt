package com.example.toolsdisplay.repository

import androidx.lifecycle.LiveData
import com.example.toolsdisplay.models.dto.ToolsInfoDto

interface ToolsInfoRepository {

    val result: LiveData<List<ToolsInfoDto>>

    val productItem: LiveData<ToolsInfoDto>

    suspend fun getToolsInfoList(pageSize: Int, sortOrder: String, field: String)

    suspend fun getProductItem(id: Int, sku: String)

}