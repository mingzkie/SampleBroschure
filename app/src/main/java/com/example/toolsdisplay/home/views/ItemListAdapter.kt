package com.example.toolsdisplay.home.views

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.toolsdisplay.R
import com.example.toolsdisplay.detailscreen.DetailScreenActivity
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.utilities.MyDiffUtil
import okhttp3.internal.notify

class ItemListAdapter(private val listItemValues: List<ToolsInfoDto>, private val context: Context,
                      private val onClickProductItem: OnClickProductItem) :
    RecyclerView.Adapter<ItemListAdapter.ToolsItemViewHolder>() {

    private val listItems: MutableList<ToolsInfoDto> = listItemValues.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ToolsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun insertList(toolsInfoListParam: List<ToolsInfoDto>) {
        var diffUtilCallback: MyDiffUtil = MyDiffUtil(listItems, toolsInfoListParam)
        var diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        listItems.addAll(toolsInfoListParam)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateList(toolsInfoListParam: List<ToolsInfoDto>) {
        var diffUtilCallback: MyDiffUtil = MyDiffUtil(listItems, toolsInfoListParam)
        var diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        listItems.clear()
        listItems.addAll(toolsInfoListParam)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getData() : List<ToolsInfoDto> {
        return listItems
    }

    override fun onBindViewHolder(holder: ToolsItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.bindData(item, position)
    }


    inner class ToolsItemViewHolder(viewParam: View) : RecyclerView.ViewHolder(viewParam) {

        val view: View = viewParam
        val thumbnail: ImageView = view.findViewById(R.id.item_thumbnail)
        val bookmark: ImageView = view.findViewById(R.id.bookmark)
        val productName: TextView = view.findViewById(R.id.item_name)
        val productPrice: TextView = view.findViewById(R.id.item_price)

        fun bindData(itemValues: ToolsInfoDto, position: Int){
            Log.d("IMAGE LINK", itemValues.imageLink)
            Glide.with(view)
                .load(itemValues.imageLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(thumbnail)

            productName.text = itemValues.name
            productPrice.text = "$".plus(itemValues.price.toString())

            if(itemValues.bookMarked == 0){
                bookmark.setImageResource(R.drawable.ic_bookmark_disable)
            } else {
                bookmark.setImageResource(R.drawable.ic_bookmark_enable)
            }

            view.setOnClickListener {
//                val intent = Intent(view.context, DetailScreenActivity::class.java).apply {
//                    putExtra(HomeActivity.SKU, itemValues.sku)
//                    putExtra(HomeActivity.PRODUCT_ID, itemValues.id)
//                }
//                view.context.startActivity(intent)
                onClickProductItem?.onClickProductItem(itemValues.id, itemValues.sku, position)
            }

        }

    }

}