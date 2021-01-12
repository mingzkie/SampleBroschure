package com.example.toolsdisplay.home.dto

import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.models.ToolsItemListResponse
import com.example.toolsdisplay.utilities.Constant

data class ToolsInfoDto(var id: Int, var name: String, var price: Float, var imageLink: String,
                       var permaLink: String, var description: String, var modelVariant: String,
                       var stockItemDto: StockItemDto?, var isBookMarked: Boolean) {


    companion object {

        private fun getCustomAttributesValuesFromResponse(customAttributesList: List<ToolsItemListResponse.CustomAttributes>, attr: CustomAttributesValues): String {
            val customAttr: ToolsItemListResponse.CustomAttributes? = customAttributesList.find {
                it.attribute_code == attr.attributesCode
            }

            return if (customAttr != null && customAttr.value is String) {
                customAttr.value as String
            } else {
                ""
            }
        }

        private fun getCustomAttributesValuesFromDB(customAttributesList: List<CustomAttributeData>, attr: CustomAttributesValues): String {
            val customAttr: CustomAttributeData? = customAttributesList.find {
                it.attributeCode == attr.attributesCode
            }

            return customAttr?.attributeValue ?: ""
        }

        @JvmStatic
        fun createFromResponse(response: List<ToolsItemListResponse.ToolsInfoItem>): List<ToolsInfoDto> {
            var resultList: MutableList<ToolsInfoDto> = ArrayList()
            for(responseItem in response){

                var imageLink : String = ""

                imageLink = if(!getCustomAttributesValuesFromResponse(responseItem.custom_attributes, CustomAttributesValues.IMAGE).isNullOrEmpty()){
                    Constant.IMAGE_LINK.plus(getCustomAttributesValuesFromResponse(responseItem.custom_attributes, CustomAttributesValues.IMAGE))
                } else {
                    Constant.NO_IMAGE_LINK
                }

                resultList.add(ToolsInfoDto(responseItem.id,
                    responseItem.name,
                    responseItem.price,
                    imageLink,
                    getCustomAttributesValuesFromResponse(responseItem.custom_attributes, CustomAttributesValues.PERMALINK),
                    getCustomAttributesValuesFromResponse(responseItem.custom_attributes, CustomAttributesValues.DESCRIPTION),
                    getCustomAttributesValuesFromResponse(responseItem.custom_attributes, CustomAttributesValues.MODEL_VARIANTS),
                    StockItemDto.createFromResponse(responseItem.stockItem, responseItem.id), false))
            }
            return resultList
        }

        @JvmStatic
        fun createFromDb(toolsFromDb: List<ToolsInfoCompleteData>): List<ToolsInfoDto> {

            var resultList: MutableList<ToolsInfoDto> = ArrayList()
            for(toolsFromDbItem in toolsFromDb){
                resultList.add(ToolsInfoDto(toolsFromDbItem.toolsInfo.id,
                    toolsFromDbItem.toolsInfo.name,
                    toolsFromDbItem.toolsInfo.price,
                    Constant.IMAGE_LINK.plus(toolsFromDbItem.mediaGalleryData[0].file),
                    getCustomAttributesValuesFromDB(toolsFromDbItem.customAttributeData, CustomAttributesValues.PERMALINK),
                    getCustomAttributesValuesFromDB(toolsFromDbItem.customAttributeData, CustomAttributesValues.DESCRIPTION),
                    getCustomAttributesValuesFromDB(toolsFromDbItem.customAttributeData, CustomAttributesValues.MODEL_VARIANTS),
                    toolsFromDbItem.stockData?.let { StockItemDto.createFromDb(it, toolsFromDbItem.toolsInfo.id) }, false))
            }
            return resultList
        }

    }




    enum class CustomAttributesValues(val attributesCode: String) {
        DESCRIPTION("description"),
        MODEL_VARIANTS("mps_model_variant"),
        PERMALINK("permalink"),
        IMAGE( "image")
    }

}