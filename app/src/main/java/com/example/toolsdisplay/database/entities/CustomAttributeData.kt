package com.example.toolsdisplay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CustomAttributeData() {
    @PrimaryKey(autoGenerate = true) var customAttrId: Int = 0
    @ColumnInfo(name = "productId") var productId : Int = 0
    @ColumnInfo(name = "attributeCode") var attributeCode: String = ""
    @ColumnInfo(name = "value") var attributeValue: String = ""
}