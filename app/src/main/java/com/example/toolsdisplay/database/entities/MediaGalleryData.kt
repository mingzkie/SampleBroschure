package com.example.toolsdisplay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MediaGalleryData(@PrimaryKey @ColumnInfo(name = "id") val id: Int,
                            @ColumnInfo(name = "productId") val productId: Int,
                            @ColumnInfo(name = "mediaType") val mediaType: String,
                            @ColumnInfo(name = "label") val label: String,
                            @ColumnInfo(name = "position") val position: Int,
                            @ColumnInfo(name = "file") val file: String,
                            @ColumnInfo(name = "disabled") val disabled: Boolean)