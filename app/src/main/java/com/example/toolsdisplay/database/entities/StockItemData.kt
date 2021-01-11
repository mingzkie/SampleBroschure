package com.example.toolsdisplay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockItemData(@PrimaryKey @ColumnInfo(name = "itemId") val itemId: Int,
                         @ColumnInfo(name = "productId") val productId : Int,
                         @ColumnInfo(name = "stockId") val stockId: Int,
                         @ColumnInfo(name = "quantity") val quantity: Int,
                         @ColumnInfo(name = "isInStock") val isInStock: Boolean)