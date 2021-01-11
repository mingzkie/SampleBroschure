package com.example.toolsdisplay.models

data class ToolsItemListResponse(var items: MutableList<ToolsInfoItem>) {

    data class ToolsInfoItem(var id: Int,
                             var sku: String,
                             var name: String,
                             var attribute_set_id: Int,
                             var price: Float,
                             var status: Int,
                             var visibility: Int,
                             var type_id: String,
                             var created_at: String,
                             var updated_at: String,
                             var weight: Int,
                             var extension_attributes: ExtensionAttributes,
                             var media_gallery_entries: MutableList<MediaGalleryEntries>,
                             var custom_attributes: MutableList<CustomAttributes>,
                             var stockItem: StockItem?)

//    data class ExtensionAttributes(var website_ids: MutableList<Int>, var category_links: MutableList<CategoryLinks>, var stock_item: StockItem)

    data class ExtensionAttributes(var stock_item: StockItem)

//    data class CategoryLinks(var position: Int, var category_id: String)
    
    data class StockItem(var item_id: Int,
                        var stock_id: Int,
                        var qty: Int,
                        var is_in_stock: Boolean,
                        var is_qty_decimal: Boolean,
                        var show_default_notification_message: Boolean,
                        var use_config_min_qty: Boolean,
                        var min_qty: Int,
                        var use_config_min_sale_qty: Int,
                        var min_sale_qty: Int,
                        var use_config_max_sale_qty: Boolean,
                        var max_sale_qty: Int,
                        var use_config_backorders: Boolean,
                        var backorders: Int,
                        var use_config_notify_stock_qty: Boolean,
                        var notify_stock_qty: Int,
                        var use_config_qty_increments: Boolean,
                        var qty_increments: Int,
                        var use_config_enable_qty_inc: Boolean,
                        var enable_qty_increments: Boolean,
                        var use_config_manage_stock: Boolean,
                        var manage_stock: Boolean,
                        var low_stock_date: String ,
                        var is_decimal_divided: Boolean,
                        var stock_status_changed_auto: Int)

    data class MediaGalleryEntries(var id: Int,
                                   var media_type: String,
                                   var label: String,
                                   var position: Int,
                                   var disabled: Boolean,
                                   var types: MutableList<String>,
                                   var file: String)

    data class CustomAttributes(var attribute_code: String,
                                var value: Any)


}