package com.example.toolsdisplay.home.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.toolsdisplay.database.entities.ToolsInfoData
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.home.repository.ToolsInfoRepository
import com.example.toolsdisplay.service.ServiceDataSourceImpl

class HomeViewModel(private val toolsInfoRepository: ToolsInfoRepository) : ViewModel() {

    var toolsInfoList: LiveData<List<ToolsInfoDto>> = toolsInfoRepository.result

    var productItem: LiveData<ToolsInfoDto> = toolsInfoRepository.productItem

    var errorEvent: LiveData<ServiceDataSourceImpl.ErrorResponseEvent> = toolsInfoRepository.errorMessage

    suspend fun fetchToolsInfoList(pageSize: Int, sortOrder: String, field: String) {
        toolsInfoRepository.getToolsInfoList(pageSize, sortOrder, field)
    }

    suspend fun getProductItem(id: Int, sku: String) {
        toolsInfoRepository.getProductItem(id, sku)
    }

    fun updateBookMark(id: Int, isBookMarked: Boolean) {
        toolsInfoRepository.updateBookmark(id, isBookMarked)
    }

    suspend fun reloadData() {
        toolsInfoRepository.reloadData()
    }

}