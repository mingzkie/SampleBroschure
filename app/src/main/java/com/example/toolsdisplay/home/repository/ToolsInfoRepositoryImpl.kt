package com.example.toolsdisplay.home.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.network.NoConnectivityException
import com.example.toolsdisplay.database.ToolsInfoDao
import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.entities.MediaGalleryData
import com.example.toolsdisplay.database.entities.StockItemData
import com.example.toolsdisplay.database.entities.ToolsInfoData
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.home.dto.ToolsInfoDto
import com.example.toolsdisplay.models.ToolsItemListResponse
import com.example.toolsdisplay.service.ServiceDataSource
import com.example.toolsdisplay.utilities.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToolsInfoRepositoryImpl(private val toolsInfoDao: ToolsInfoDao,
                              private val serviceDataSource: ServiceDataSource): ToolsInfoRepository {

    var _listItem = MutableLiveData<List<ToolsInfoDto>>()
    override val result: LiveData<List<ToolsInfoDto>>
        get() = _listItem

    override suspend fun getToolsInfoList(pageSize: Int, sortOrder: String, field: String) {
        GlobalScope.launch(Dispatchers.IO ) {
              if(!toolsInfoDao.getToolsList().isNullOrEmpty()){
                  Log.d("ToolsInfoRepositoryImpl", "ToolsInfoRepositoryImpl: Getting from persistence")
                 _listItem.postValue(ToolsInfoDto.createFromDb(toolsInfoDao.getToolsList()))
              } else {
                  Log.d("ToolsInfoRepositoryImpl", "ToolsInfoRepositoryImpl: Getting from API")
                  serviceDataSource.getToolsList(pageSize, sortOrder, field, Constant.Companion.AUTH_BEARER_KEY_NAME.plus(toolsInfoDao.getAccessToken().authToken))
                  persistResponse()
              }
        }
    }

    private fun persistResponse() {
        GlobalScope.launch(Dispatchers.Main ) {
            serviceDataSource.toolsList.observeForever { newData ->
//                val sortedResult = newData.sortedBy { fetchedDataItem -> fetchedDataItem.trackId }
                    _listItem.postValue(ToolsInfoDto.createFromResponse(newData))
                    persistFetchedData(newData)
            }
        }

    }

    private fun persistFetchedData(toolsList: List<ToolsItemListResponse.ToolsInfoItem>) {
          for(toolsListItem in toolsList) {
              parseInfoData(toolsListItem)
          }
    }


    private fun parseInfoData(responseItem: ToolsItemListResponse.ToolsInfoItem) {
        var id = responseItem.id
        GlobalScope.launch(Dispatchers.IO){

            toolsInfoDao.insertToolsInfo(ToolsInfoData(responseItem.id, responseItem.sku, responseItem.name,
                responseItem.attribute_set_id, responseItem.price,
                responseItem.status, responseItem.visibility, responseItem.type_id,
                responseItem.created_at, responseItem.updated_at, responseItem.weight))

            for (mediaItem in responseItem.media_gallery_entries) {
                toolsInfoDao.insertMediaGallery(
                    MediaGalleryData(
                        mediaItem.id, id, mediaItem.media_type,
                        mediaItem.label, mediaItem.position,
                        mediaItem.file, mediaItem.disabled
                    )
                )
            }

            for (customAttr in responseItem.custom_attributes) {
                var customAttributeData =  CustomAttributeData()
                customAttributeData.productId = id
                customAttributeData.attributeCode = customAttr.attribute_code
                if(customAttr.value is String) {
                    customAttributeData.attributeValue = customAttr.value as String
                } else {
                    customAttributeData.attributeValue = ""
                }
                toolsInfoDao.insertCustomAttributeData(customAttributeData)
            }

            if(responseItem.stockItem != null) {
                toolsInfoDao.insertStockItemData(
                    StockItemData(
                        responseItem.stockItem!!.item_id, id,
                        responseItem.stockItem!!.stock_id, responseItem.stockItem!!.qty,
                        responseItem.stockItem!!.is_in_stock
                    )
                )
            }

        }

    }


}