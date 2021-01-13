package com.example.toolsdisplay.home.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.repository.ToolsInfoRepository

class HomeViewModel(private val toolsInfoRepository: ToolsInfoRepository) : ViewModel() {

    var toolsInfoList: LiveData<List<ToolsInfoDto>> = toolsInfoRepository.result

    var productItem: LiveData<ToolsInfoDto> = toolsInfoRepository.productItem


    suspend fun fetchToolsInfoList(pageSize: Int, sortOrder: String, field: String) {
        toolsInfoRepository.getToolsInfoList(pageSize, sortOrder, field)
    }

    suspend fun getProductItem(id: Int, sku: String) {
        toolsInfoRepository.getProductItem(id, sku)
    }

}