package com.example.toolsdisplay.utilities

import androidx.recyclerview.widget.DiffUtil
import com.example.toolsdisplay.models.dto.ToolsInfoDto

class MyDiffUtil(oldListParam: List<ToolsInfoDto>, newListParam: List<ToolsInfoDto>) : DiffUtil.Callback() {

    private var oldList: List<ToolsInfoDto> = oldListParam
    private var newList: List<ToolsInfoDto> = newListParam


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}