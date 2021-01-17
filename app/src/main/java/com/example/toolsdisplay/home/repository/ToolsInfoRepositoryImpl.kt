package com.example.toolsdisplay.home.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toolsdisplay.database.ToolsInfoDao
import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.entities.MediaGalleryData
import com.example.toolsdisplay.database.entities.StockItemData
import com.example.toolsdisplay.database.entities.ToolsInfoData
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.models.ToolsItemListResponse
import com.example.toolsdisplay.service.ServiceDataSource
import com.example.toolsdisplay.service.ServiceDataSourceImpl
import com.example.toolsdisplay.utilities.Constant
import com.example.toolsdisplay.utilities.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToolsInfoRepositoryImpl(private val toolsInfoDao: ToolsInfoDao,
                              private val serviceDataSource: ServiceDataSource): ToolsInfoRepository {

    var _listItem = MutableLiveData<List<ToolsInfoDto>>()
    override val result: LiveData<List<ToolsInfoDto>>
        get() = _listItem

    var _productItem = MutableLiveData<ToolsInfoDto>()
    override val productItem: LiveData<ToolsInfoDto>
        get() = _productItem

    var _errorMessage = MutableLiveData<ServiceDataSourceImpl.ErrorResponseEvent>()
    override val errorMessage: LiveData<ServiceDataSourceImpl.ErrorResponseEvent>
        get() = _errorMessage

    init {
        serviceDataSource.errorMessage.observeForever { errorMessageEvent ->
            _errorMessage.postValue(errorMessageEvent) }
    }


    override suspend fun getToolsInfoList(pageSize: Int, sortOrder: String, field: String) {
        GlobalScope.launch(Dispatchers.IO ) {
              if(!toolsInfoDao.getToolsList().isNullOrEmpty()){
                  Log.d("ToolsInfoRepositoryImpl", "ToolsInfoRepositoryImpl: Getting from persistence")
                 _listItem.postValue(ToolsInfoDto.createFromDb(toolsInfoDao.getToolsList()))
              } else {
                  Log.d("ToolsInfoRepositoryImpl", "ToolsInfoRepositoryImpl: Getting from API")
                  serviceDataSource.getToolsList(pageSize, sortOrder, field, Constant.Companion.AUTH_BEARER_KEY_NAME.plus(toolsInfoDao.getAccessToken().authToken))
                  persistProductListResponse()
              }
        }
    }

    private fun persistProductListResponse() {
        GlobalScope.launch(Dispatchers.Main ) {
            serviceDataSource.toolsList.observeForever { newData ->
//                val sortedResult = newData.sortedBy { fetchedDataItem -> fetchedDataItem.trackId }
                    _listItem.postValue(ToolsInfoDto.createFromResponse(newData))
                    persistFetchedData(newData)
            }
        }

    }

    override suspend fun getProductItem(id: Int, sku: String) {
        GlobalScope.launch(Dispatchers.IO) {
            if(toolsInfoDao.getProductItem(id) != null && toolsInfoDao.getProductItem(id).stockData != null) {
               _productItem.postValue(ToolsInfoDto.createFromDbItem(toolsInfoDao.getProductItem(id)))
            } else {
                serviceDataSource.getProductItem(sku, Constant.Companion.AUTH_BEARER_KEY_NAME.plus(toolsInfoDao.getAccessToken().authToken))
                persistProductItemResponse()
            }
        }
    }

    override fun updateBookmark(id: Int, isBookMarked: Boolean) {
        Log.d("TOOLS REPO", "UPDATE BOOKMARK")
        GlobalScope.launch(Dispatchers.IO) {
            toolsInfoDao.updateBookmark(id,isBookMarked)
        }
    }

    override suspend fun reloadData() {
        GlobalScope.launch(Dispatchers.IO ) {
            if (!toolsInfoDao.getToolsList().isNullOrEmpty()) {
                Log.d("ToolsInfoRepositoryImpl", "Reloading List")
                _listItem.postValue(ToolsInfoDto.createFromDb(toolsInfoDao.getToolsList()))
            }
        }
    }

    private fun persistProductItemResponse() {
        GlobalScope.launch(Dispatchers.Main ) {
            serviceDataSource.productItem.observeForever { newData ->
                _productItem.postValue(ToolsInfoDto.createFromResponseItem(newData))
                parseInfoData(newData)
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
                responseItem.created_at, responseItem.updated_at, responseItem.weight, false))

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