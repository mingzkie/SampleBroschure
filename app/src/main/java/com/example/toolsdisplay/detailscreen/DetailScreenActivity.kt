package com.example.toolsdisplay.detailscreen

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.detailscreen.views.DetailViewModel
import com.example.toolsdisplay.detailscreen.views.DetailViewModelFactory
import com.example.toolsdisplay.home.views.HomeActivity
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class DetailScreenActivity : ScopeActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val detailViewModelFactory by instance<DetailViewModelFactory>()

    private lateinit var skeletonScreen: ViewSkeletonScreen
    private lateinit var thumbnail: ImageView
    private lateinit var  productName: TextView
    private lateinit var  productPrice: TextView
    //private lateinit var overflow: ImageView

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_screen)
        supportActionBar?.hide()

        setUpView()

        val sku = intent.getStringExtra(HomeActivity.SKU)
        val id = intent.getIntExtra(HomeActivity.PRODUCT_ID, 0)
        detailViewModel = ViewModelProvider(this, detailViewModelFactory).get(DetailViewModel::class.java)
        if(!sku.isNullOrEmpty()) {
            initFetchData(id, sku)
        }

    }

    private fun setUpView() {
        thumbnail = findViewById(R.id.detail_thumbnail)
        productName = findViewById(R.id.detail_name)
        productPrice = findViewById(R.id.detail_price)
    }

    private fun initFetchData(id: Int, sku: String) = launch {
        showShimmer()
        detailViewModel.getProductItem(id, sku)
        updateView()
    }

    private fun updateView(){
        detailViewModel.productItem.observe(this, Observer { productData ->
            if(productData == null) return@Observer
            hideShimmer()

            if(productData != null){
                Glide.with(this)
                    .load(productData.imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(thumbnail)

                productName.text = productData.name
                productPrice.text = "$".plus(productData.price.toString())
            }

        })
    }

    private fun showShimmer() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        skeletonScreen = Skeleton.bind(viewGroup)
            .load(R.layout.layout_shimmer)
            .show()
    }

    private fun hideShimmer(){
        skeletonScreen?.hide()
    }



}