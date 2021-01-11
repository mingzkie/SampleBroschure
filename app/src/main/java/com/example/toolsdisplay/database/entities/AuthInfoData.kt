package com.example.toolsdisplay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthInfoData(@PrimaryKey var id: Int,
                        @ColumnInfo(name = "authToken") val authToken: String)