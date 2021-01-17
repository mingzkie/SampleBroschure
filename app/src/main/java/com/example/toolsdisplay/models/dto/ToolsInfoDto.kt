package com.example.toolsdisplay.models.dto

import com.example.toolsdisplay.database.entities.CustomAttributeData
import com.example.toolsdisplay.database.relations.ToolsInfoCompleteData
import com.example.toolsdisplay.models.ToolsItemListResponse
import com.example.toolsdisplay.utilities.Constant

data class ToolsInfoDto(var id: Int, var name: String, var sku: String, var price: Float, var imageLink: String,
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
                resultList.add(createFromResponseItem(responseItem))
            }
            return resultList
        }

        @JvmStatic
        fun createFromDb(toolsFromDb: List<ToolsInfoCompleteData>): List<ToolsInfoDto> {

            var resultList: MutableList<ToolsInfoDto> = ArrayList()
            for(toolsFromDbItem in toolsFromDb){
                resultList.add(createFromDbItem(toolsFromDbItem))
            }
            return resultList
        }

        @JvmStatic
        fun createFromResponseItem(productItem: ToolsItemListResponse.ToolsInfoItem) : ToolsInfoDto {

            var imageLink = if(!getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.IMAGE).isNullOrEmpty()){
                Constant.IMAGE_LINK.plus(getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.IMAGE))
            } else {
                Constant.NO_IMAGE_LINK
            }

            var description = if(!getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.DESCRIPTION).isNullOrEmpty()){
                getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.DESCRIPTION)
            } else {
                "No available description"
            }

            return ToolsInfoDto(productItem.id,
                productItem.name,
                productItem.sku,
                productItem.price,
                imageLink,
                getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.PERMALINK),
                description,
                getCustomAttributesValuesFromResponse(productItem.custom_attributes, CustomAttributesValues.MODEL_VARIANTS),
                StockItemDto.createFromResponse(productItem.stockItem, productItem.id), false)


        }

        @JvmStatic
        fun createFromDbItem(productItem: ToolsInfoCompleteData) : ToolsInfoDto {

            var imageLink = if(!getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.IMAGE).isNullOrEmpty()){
                Constant.IMAGE_LINK.plus(getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.IMAGE))
            } else {
                Constant.NO_IMAGE_LINK
            }

            var description = if(!getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.DESCRIPTION).isNullOrEmpty()){
                getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.DESCRIPTION)
            } else {
                "No available description"
            }

            return ToolsInfoDto(productItem.toolsInfo.id,
                productItem.toolsInfo.name,
                productItem.toolsInfo.sku,
                productItem.toolsInfo.price,
                imageLink,
                getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.PERMALINK),
                getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.DESCRIPTION),
                getCustomAttributesValuesFromDB(productItem.customAttributeData, CustomAttributesValues.MODEL_VARIANTS),
                productItem.stockData?.let { StockItemDto.createFromDb(it, productItem.toolsInfo.id) }, false)

        }

    }




    enum class CustomAttributesValues(val attributesCode: String) {
        DESCRIPTION("description"),
        MODEL_VARIANTS("mps_model_variant"),
        PERMALINK("permalink"),
        IMAGE( "image")
    }

}