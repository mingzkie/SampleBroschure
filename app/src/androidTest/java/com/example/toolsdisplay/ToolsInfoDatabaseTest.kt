package com.example.toolsdisplay

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.toolsdisplay.database.ToolsInfoDao
import com.example.toolsdisplay.database.ToolsRoomDatabase
import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.entities.MediaGalleryData
import com.example.toolsdisplay.database.entities.StockItemData
import com.example.toolsdisplay.database.entities.ToolsInfoData
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.Executors

class ToolsInfoDatabaseTest {

      private lateinit var toolsInfoDao: ToolsInfoDao
      private lateinit var db : ToolsRoomDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db= Room.inMemoryDatabaseBuilder(context, ToolsRoomDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
         toolsInfoDao = db.toolsDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertData() = runBlocking {
           assertNull(toolsInfoDao.getProductItem(1))
        toolsInfoDao.insertToolsInfo(
            ToolsInfoData(2, "M18 FCSRH66-0B0", "M18 FUEL™ 190mm Rear Handle Circular Saw",
            4, 730.0F,
            1, 1, "simple",
                "2020-11-23 02:35:05", "2020-12-11 06:06:02", 1, false, 0)
        )

        toolsInfoDao.insertMediaGallery(
            MediaGalleryData(
                3, 1, "image",
                "M18-FCSRH66-121-Hero01.png", 3,
                "/m/1/m18-fcsrh66-121-hero01.png", false
            )
        )

        var customAttributeData =  CustomAttributeData()
        customAttributeData.productId = 2
        customAttributeData.attributeCode = "image"
        customAttributeData.attributeValue = "/m/1/m18-fcsrh66-121-hero01.png"
        toolsInfoDao.insertCustomAttributeData(customAttributeData)

        toolsInfoDao.insertStockItemData(
            StockItemData(
                1, 2,
                1, 100,
                true
            )
        )

        assertNotNull(toolsInfoDao.getProductItem(2).toolsInfo)
        assertNotNull(toolsInfoDao.getProductItem(2).mediaGalleryData)
        assertNotNull(toolsInfoDao.getProductItem(2).customAttributeData)
        assertNotNull(toolsInfoDao.getProductItem(2).stockData)

    }

    @Test
    @Throws(Exception::class)
    fun willInsertListData() = runBlocking {
        assertEquals(0, toolsInfoDao.getToolsList().size)
        for(i in 1..5) {
            toolsInfoDao.insertToolsInfo(
                ToolsInfoData(i, "M18 FCSRH66-0B0", "M18 FUEL™ 190mm Rear Handle Circular Saw",
                    4, 730.0F,
                    1, 1, "simple",
                    "2020-11-23 02:35:05", "2020-12-11 06:06:02", 1, false, 0)
            )
        }

        toolsInfoDao.insertToolsInfo(
            ToolsInfoData(6, "M18 FBJS-0X0", "M18 FUEL™ Body Grip Jigsaw",
                4, 780.0F,
                1, 1, "simple",
                "2020-11-23 02:35:05", "2020-12-11 06:06:02", 1, false, 0)
        )

        //Check the size of persisted list matches with number of data inserted
        Assert.assertEquals(6, toolsInfoDao.getToolsList().size)
        Assert.assertEquals("M18 FBJS-0X0", toolsInfoDao.getToolsList()[5].toolsInfo.sku)

    }

    @Test
    @Throws(Exception::class)
    fun willUpdateBookmark() = runBlocking {
        toolsInfoDao.insertToolsInfo(
            ToolsInfoData(2, "M18 FCSRH66-0B0", "M18 FUEL™ 190mm Rear Handle Circular Saw",
                4, 730.0F,
                1, 1, "simple",
                "2020-11-23 02:35:05", "2020-12-11 06:06:02", 1, false, 0)
        )
        Assert.assertEquals(0, toolsInfoDao.getToolsList()[0].toolsInfo.bookMarked)
        toolsInfoDao.update(
            ToolsInfoData(2, "M18 FCSRH66-0B0", "M18 FUEL™ 190mm Rear Handle Circular Saw",
                4, 730.0F,
                1, 1, "simple",
                "2020-11-23 02:35:05", "2020-12-11 06:06:02", 1, false, 1)
        )
        Assert.assertEquals(1, toolsInfoDao.getToolsList()[0].toolsInfo.bookMarked)
    }

}