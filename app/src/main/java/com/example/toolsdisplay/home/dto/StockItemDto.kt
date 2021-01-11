package com.example.toolsdisplay.home.dto

import com.example.toolsdisplay.database.entities.StockItemData
import com.example.toolsdisplay.models.ToolsItemListResponse

data class StockItemDto(var itemId: Int?,
                        var productId : Int?,
                        var stockId: Int?,
                        var quantity: Int?,
                        var isInStock: Boolean?) {

    companion object {
        @JvmStatic
        fun createFromResponse(stockItemParams: ToolsItemListResponse.StockItem?, productId: Int) : StockItemDto {
            return StockItemDto(stockItemParams?.item_id, productId, stockItemParams?.stock_id,
                                stockItemParams?.qty, stockItemParams?.is_in_stock)
        }

        @JvmStatic
        fun createFromDb(stockItemData: StockItemData, productId: Int) : StockItemDto {
            return StockItemDto(stockItemData.itemId, productId, stockItemData.stockId,
                stockItemData.quantity, stockItemData.isInStock)
        }
    }

}

