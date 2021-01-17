package com.example.toolsdisplay.detailscreen.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.home.repository.ToolsInfoRepository

class DetailViewModel(private val toolsInfoRepository: ToolsInfoRepository) : ViewModel() {

    var productItem: LiveData<ToolsInfoDto> = toolsInfoRepository.productItem

    suspend fun getProductItem(id: Int, sku: String) {
        toolsInfoRepository.getProductItem(id, sku)
    }

}