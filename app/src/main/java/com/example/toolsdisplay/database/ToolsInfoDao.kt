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
    @Query("SELECT * FROM ToolsInfoData")
    fun getToolsList() : List<ToolsInfoCompleteData>

    @Transaction
    @Query("SELECT * FROM ToolsInfoData WHERE id=:id")
    fun getProductItem(id: Int) : ToolsInfoCompleteData

    @Query("UPDATE ToolsInfoData SET bookMarked = :bookMarked WHERE id=:id")
    fun updateBookmark(id: Int, bookMarked: Int)

    @Query("SELECT * FROM AuthInfoData")
    suspend fun getAccessToken() : AuthInfoData

    @Update
    suspend fun update(toolsInfoData: ToolsInfoData)

}