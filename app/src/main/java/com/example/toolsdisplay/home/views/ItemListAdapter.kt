package com.example.toolsdisplay.home.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.toolsdisplay.R
import com.example.toolsdisplay.home.dto.ToolsInfoDto

class ItemListAdapter(private val listItemValues: List<ToolsInfoDto>, private val context: Context) :
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

    fun updateList(toolsInfoListParam: List<ToolsInfoDto>) {
       this.listItems.clear()
       this.listItems.addAll(toolsInfoListParam)
       notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ToolsItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.bindData(item)
    }


    inner class ToolsItemViewHolder(viewParam: View) : RecyclerView.ViewHolder(viewParam) {

        val view: View = viewParam
        val thumbnail: ImageView = view.findViewById(R.id.item_thumbnail)
        val overflow: ImageView = view.findViewById(R.id.item_overflow)
        val productName: TextView = view.findViewById(R.id.item_name)
        val productPrice: TextView = view.findViewById(R.id.item_price)

        fun bindData(itemValues: ToolsInfoDto){
            Log.d("IMAGE LINK", itemValues.imageLink)
            Glide.with(view)
                .load(itemValues.imageLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(thumbnail)

            productName.text = itemValues.name
            productPrice.text = "$".plus(itemValues.price.toString())

        }

    }

}