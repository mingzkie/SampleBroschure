package com.example.toolsdisplay.home.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toolsdisplay.home.repository.ToolsInfoRepository

class HomeViewModelFactory(private val toolsInfoRepository: ToolsInfoRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(toolsInfoRepository) as T
    }
}