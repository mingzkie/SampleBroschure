package com.example.toolsdisplay.home.views

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.widget.Button
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
import com.example.toolsdisplay.models.dto.ToolsInfoDto
import com.example.toolsdisplay.service.ServiceDataSourceImpl
import com.example.toolsdisplay.utilities.Constant
import com.example.toolsdisplay.utilities.FileUtilities
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.io.IOException
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

    private lateinit var loadingDialog: Dialog
    private lateinit var popUpDialog: Dialog

    private lateinit var thumbnail: ImageView
    private lateinit var close: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var webViewDetail: WebView
    private lateinit var bookMarked: ImageView

    private lateinit var errorDialog: Dialog
    private lateinit var errorTitle: TextView
    private lateinit var errorMessage: TextView
    private lateinit var dialogOkButton: Button

    private lateinit var reloadButton: Button
    private lateinit var sortButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpView()
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        watchError()
        initFetchData()

    }

    private fun initFetchData() {
        showShimmer()
        fetchData()
    }

    private fun fetchData() = launch {
         homeViewModel.fetchToolsInfoList(10, Constant.SORT_ORDER, Constant.ENTITY_ID)
         initChangeView()
    }

    private fun watchError() {
        homeViewModel.errorEvent.observe(this, Observer { errorEvent ->
            hideShimmer()
            hideLoadingDialog()
            showErrorDialog(errorEvent)
        })
    }

    private fun initChangeView() {
        homeViewModel.toolsInfoList.observe(this, Observer { toolsInfoList ->
            if(toolsInfoList== null) return@Observer
            hideShimmer()
            popUpDialog.dismiss()
            listData = toolsInfoList.toMutableList()
            if(!listData.isNullOrEmpty()){
                itemRecyclerView.post(Runnable {
                    this.adapter.insertList(listData)
                    this.adapter.notifyDataSetChanged()
                })
            }

        })

    }

    private fun updateView(position: Int, bookMarked: Boolean) {
        showShimmer()
        this.listData = adapter.getData().toMutableList()
        this.listData[position].isBookMarked = bookMarked
        itemRecyclerView.post(Runnable {
            adapter.updateList(listData)
            adapter.notifyDataSetChanged()
        })
    }

    private fun reloadData()  {
        initFetchData()
    }

    private fun sortData() = launch {
        listData.sortedWith(compareBy {it.name})
        itemRecyclerView.post(Runnable {
            adapter.updateList(listData)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setUpView() {
        var toolbar: Toolbar = findViewById(R.id.home_toolbar)
        toolbar.title = getString(R.string.home_title)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        }

        reloadButton = findViewById(R.id.button_reload)
        sortButton = findViewById(R.id.button_sort)

        reloadButton.setOnClickListener { reloadData() }
        sortButton.setOnClickListener { sortData() }

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

        //Loading dialog
        loadingDialog = Dialog(this, R.style.ProgressDialogTheme)
        loadingDialog.setCanceledOnTouchOutside(false)

        //Pop up view
        popUpDialog = Dialog(this)
        popUpDialog.setContentView(R.layout.activity_detail_screen)

        this.thumbnail = popUpDialog.findViewById(R.id.detail_thumbnail)
        this.close = popUpDialog.findViewById(R.id.detail_close)
        this.productName = popUpDialog.findViewById(R.id.detail_name)
        this.productPrice = popUpDialog.findViewById(R.id.detail_price)
        this.webViewDetail = popUpDialog.findViewById(R.id.web_details)
        this.bookMarked = popUpDialog.findViewById(R.id.detail_bookmark)

        close.setOnClickListener {
            popUpDialog.dismiss()
            hideShimmer()
        }


        //Error Dialog
        errorDialog = Dialog(this, R.style.ProgressDialogTheme)
        errorDialog.setContentView(R.layout.error_layout)
        this.errorTitle = errorDialog.findViewById(R.id.error_title)
        this.errorMessage = errorDialog.findViewById(R.id.error_message)
        this.dialogOkButton = errorDialog.findViewById(R.id.ok_button)

        dialogOkButton.setOnClickListener {
            popUpDialog.dismiss()
            hideErrorDialog()
            hideLoadingDialog()
            hideShimmer()
        }

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

    override fun onClickProductItem(id: Int, sku: String, position: Int) {
        showLoadingDialog()
        lifecycleScope.launch {
            homeViewModel.getProductItem(id, sku)
            showPopUp(position)
        }

    }

    private fun showPopUp(position: Int){
        homeViewModel.productItem.observe(this, Observer { productData ->
            if(productData == null) return@Observer


            if(productData != null){
                hideShimmer()
                hideLoadingDialog()
                hideErrorDialog()

                Glide.with(this)
                    .load(productData.imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_placeholder_image)
//                    .placeholder(R.drawable.progress_animation)
                    .into(thumbnail)

                productName.text = productData.name
                productPrice.text = "$".plus(productData.price.toString())

                if(productData.isBookMarked){
                    bookMarked.setImageResource(R.drawable.ic_bookmark_enable)
                } else {
                    bookMarked.setImageResource(R.drawable.ic_bookmark_disable)
                }

                if(!productData.description.isNullOrEmpty()){
                    webViewDetail.loadData(getWebPageContent(productData.description), "text/html; charset=utf-8", "utf-8")
                    webViewDetail.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                             var height = webViewDetail.contentHeight
                             if(height != 0) {
                                 webViewDetail.viewTreeObserver.removeOnPreDrawListener(this)
                             }
                            return false
                        }
                    })
                }

                var bookMarkChanged: Boolean
                bookMarked.setOnClickListener {
                    if(productData.isBookMarked){
                        bookMarkChanged = false
                        bookMarked.setImageResource(R.drawable.ic_bookmark_disable)
                    } else {
                        bookMarkChanged = true
                        bookMarked.setImageResource(R.drawable.ic_bookmark_enable)
                    }

                    homeViewModel.updateBookMark(position, bookMarkChanged)
                    updateView(position, bookMarkChanged)

                }
                popUpDialog.show()

            }

        })



    }

    private fun getWebPageContent(details: String) : String {
        val webDetails = details.replace("\\r","").replace("\\t","")
        try {
            var template = FileUtilities.readFile2(assets.open("template.html"))
            return template!!.replace("CONTENT_CONTENT_CONTENT", webDetails)
        } catch (e: IOException) {
            return webDetails
        }
    }

    private fun showLoadingDialog() {
        loadingDialog.setContentView(R.layout.loading_indicator)
        loadingDialog.setCancelable(false)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    private fun showErrorDialog(errorEventParam: ServiceDataSourceImpl.ErrorResponseEvent){
        if(errorEventParam == ServiceDataSourceImpl.ErrorResponseEvent.CONNECTION_EROR) {
            this.errorTitle.text = getString(R.string.error_connection_title)
            this.errorMessage.text = getString(R.string.error_connection_message)
        } else {
            this.errorTitle.text = getString(R.string.error_generic_title)
            this.errorMessage.text = getString(R.string.error_generic_message)
        }

        errorDialog.show()

    }

    private fun hideErrorDialog(){
        errorDialog.dismiss()
    }

}