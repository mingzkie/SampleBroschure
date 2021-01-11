package com.example.toolsdisplay.home.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toolsdisplay.home.dto.ToolsInfoDto
import com.example.toolsdisplay.home.repository.ToolsInfoRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val toolsInfoRepository: ToolsInfoRepository) : ViewModel() {

    var toolsInfoList: LiveData<List<ToolsInfoDto>> = toolsInfoRepository.result


    suspend fun fetchToolsInfoList(pageSize: Int, sortOrder: String, field: String) {
        toolsInfoRepository.getToolsInfoList(pageSize, sortOrder, field)
    }

}