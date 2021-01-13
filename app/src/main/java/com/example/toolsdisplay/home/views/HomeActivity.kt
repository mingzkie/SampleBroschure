package com.example.toolsdisplay.home.views

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.detailscreen.DetailScreenActivity
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.utilities.Constant
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : ScopeActivity(), KodeinAware, OnClickProductItem {

    companion object {
        const val SKU = "sku"
        const val PRODUCT_ID = "productId"
    }

    override val kodein by closestKodein()
    private val homeViewModelFactory by instance<HomeViewModelFactory>()

    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var adapter: ItemListAdapter
    private var listData: MutableList<ToolsInfoDto> = ArrayList()

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private lateinit var popUpDialog: Dialog

    private lateinit var thumbnail: ImageView
    private lateinit var close: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpView()
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        initFetchData()

    }

    private fun initFetchData() = launch {
         showShimmer()
         homeViewModel.fetchToolsInfoList(10, Constant.SORT_ORDER, Constant.ENTITY_ID)
         updateView()
    }

    private fun updateView(){
        homeViewModel.toolsInfoList.observe(this, Observer { toolsInfoList ->
            if(toolsInfoList== null) return@Observer
            hideShimmer()
            if(!toolsInfoList.isNullOrEmpty()){
               this.itemRecyclerView.post( Runnable {
                     listData.clear()
                     listData.addAll(toolsInfoList)
                     adapter.updateList(listData)
               })
            }

        })
    }

    private fun setUpView() {
        var toolbar: Toolbar = findViewById(R.id.home_toolbar)
        toolbar.title = getString(R.string.home_title)
        setSupportActionBar(toolbar)
        popUpDialog = Dialog(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        }

        itemRecyclerView = findViewById(R.id.tools_recyclerview)
        adapter = ItemListAdapter(ArrayList(),this,this)
        var layoutManager = GridLayoutManager(this, 2)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                dpToPx(10),
                true
            )
        )
        itemRecyclerView.itemAnimator = DefaultItemAnimator()
        itemRecyclerView.adapter = adapter

        //Pop up view
        popUpDialog.setContentView(R.layout.activity_detail_screen)

        this.thumbnail = popUpDialog.findViewById(R.id.detail_thumbnail)
        this.close = popUpDialog.findViewById(R.id.detail_close)
        this.productName = popUpDialog.findViewById(R.id.detail_name)
        this.productPrice = popUpDialog.findViewById(R.id.detail_price)

    }

    private fun dpToPx(dp: Int) : Int
    {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.fromBits(dp), resources.displayMetrics))
    }

    private fun showShimmer() {
        this.skeletonScreen = Skeleton.bind(this.itemRecyclerView)
            .adapter(adapter)
            .load(R.layout.layout_default_item_skeleton)
            .shimmer(true)
            .show()
    }

    private fun hideShimmer(){
        skeletonScreen?.hide()
    }

    override fun onClickProductItem(id: Int, sku: String) {
//        val intent = Intent(this, DetailScreenActivity::class.java).apply {
//            putExtra(HomeActivity.SKU, sku)
//            putExtra(HomeActivity.PRODUCT_ID, id)
//        }
//        startActivity(intent)
//          popUpDialog.setContentView(R.layout.activity_detail_screen)
        clearPopUpView()
        lifecycleScope.launch {
            homeViewModel.getProductItem(id, sku)
            showPopUp()
        }

    }

    private fun showPopUp(){
        homeViewModel.productItem.observe(this, Observer { productData ->
            if(productData == null) return@Observer
            hideShimmer()

            if(productData != null){
                Glide.with(this)
                    .load(productData.imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_placeholder_image)
//                    .placeholder(R.drawable.progress_animation)
                    .into(thumbnail)

                productName.text = productData.name
                productPrice.text = "$".plus(productData.price.toString())
            }

        })

        close.setOnClickListener {
            popUpDialog.dismiss()
        }

        popUpDialog.show()

    }

    private fun clearPopUpView() {
        Glide.with(this).clear(thumbnail)
        productName.text = ""
        productPrice.text = ""
    }

//    private fun inputData(): List<ToolsInfoDto> {
//
//        var imageLink = "https://dtnstgmilwaukeetoolasia.kinsta.cloud/wp-content/uploads/2020/11/"
//        var permalink = "https://dtnstgmilwaukeetoolasia.kinsta.cloud/product/m18-fuel-body-grip-jigsaw/?attribute_model=M18+FBJS-0X0"
//        var description = "<ul>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Strong magnet at start of the tape allows easy hooking onto flat surfaces and round pipe</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Compact and ergonomic design</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Improved standout up to 3.6 m</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Nylon non-reflective bond blade protection, up to 10x more resistant to jobsite damage</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>27 mm blade with double sided printing on tape for easier readability</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Patented finger stop: stable when stands alone</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>5-point reinforced frame for jobsite durability</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Class II accuracy</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<li>Architect scale provides easy calculating of scaled drawings</li>\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</ul>"
//        var modelVariant = "M18 FUEL™ Body Grip Jigsaw"
//
//           var toolsItem = ToolsInfoDto(2, "M18 FUEL™ 190mm Rear Handle Circular Saw",
//               730.00, imageLink.plus("M18-FCSRH66-121-Hero01.png"), permalink, description, modelVariant)
//
//        for (i in 1..5) {
//           listData.add(toolsItem)
//        }
//
//        return listData
//
//    }

}