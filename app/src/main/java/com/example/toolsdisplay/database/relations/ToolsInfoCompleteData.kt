package com.example.toolsdisplay.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.entities.MediaGalleryData
import com.example.toolsdisplay.database.entities.StockItemData
import com.example.toolsdisplay.database.entities.ToolsInfoData

data class ToolsInfoCompleteData(
    @Embedded val toolsInfo: ToolsInfoData,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val stockData: StockItemData?,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val mediaGalleryData: List<MediaGalleryData>,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val customAttributeData: List<CustomAttributeData>

)