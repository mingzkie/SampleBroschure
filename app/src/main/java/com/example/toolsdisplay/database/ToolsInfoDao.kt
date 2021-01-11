package com.example.toolsdisplay.database

import androidx.room.*
import com.example.toolsdisplay.database.entities.*
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData

@Dao
interface ToolsInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(authData: AuthInfoData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToolsInfo(toolsInfoData: ToolsInfoData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockItemData(stockItemData: StockItemData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaGallery(mediaGalleryData: MediaGalleryData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomAttributeData(mediaGalleryData: CustomAttributeData)

    @Transaction
    @Query("SELECT * from ToolsInfoData")
    fun getToolsList() : List<ToolsInfoCompleteData>

    @Query("SELECT * from AuthInfoData")
    fun getAccessToken() : AuthInfoData

}