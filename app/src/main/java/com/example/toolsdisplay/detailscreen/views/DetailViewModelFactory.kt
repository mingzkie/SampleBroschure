package com.example.toolsdisplay.detailscreen.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toolsdisplay.home.views.HomeViewModel
import com.example.toolsdisplay.repository.ToolsInfoRepository

class DetailViewModelFactory(private val toolsInfoRepository: ToolsInfoRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(toolsInfoRepository) as T
    }
}