package com.example.toolsdisplay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToolsInfoData(@PrimaryKey @ColumnInfo(name = "id") val id: Int,
                         @ColumnInfo(name = "sku") val sku: String,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "attribute_set_id") val attribute_set_id: Int,
                         @ColumnInfo(name = "price") val price: Float,
                         @ColumnInfo(name = "status") val status: Int,
                         @ColumnInfo(name = "visibility") val visibility: Int,
                         @ColumnInfo(name = "type_id") val type_id: String,
                         @ColumnInfo(name = "created_at") val created_at: String,
                         @ColumnInfo(name = "updated_at") val updated_at: String,
                         @ColumnInfo(name = "weight") val weight: Int,
                         @ColumnInfo(name = "isBookMarked") val isBookMarked: Boolean)