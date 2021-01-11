package com.example.toolsdisplay.home.repository

import androidx.lifecycle.LiveData
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.home.dto.ToolsInfoDto
import com.example.toolsdisplay.models.ToolsItemListResponse

interface ToolsInfoRepository {

    val result: LiveData<List<ToolsInfoDto>>

    suspend fun getToolsInfoList(pageSize: Int, sortOrder: String, field: String)

}