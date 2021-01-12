package com.example.toolsdisplay.home.views

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.lifecycle.Observer
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.home.dto.ToolsInfoDto
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : ScopeActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val homeViewModelFactory by instance<HomeViewModelFactory>()

    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var adapter: ItemListAdapter
    private var listData: MutableList<ToolsInfoDto> = ArrayList()

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpView()
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        initFetchData()

    }

    private fun initFetchData() = launch {
         showShimmer()
         homeViewModel.fetchToolsInfoList(10,"ASC", "entity_id")
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        }

        itemRecyclerView = findViewById(R.id.tools_recyclerview)
        adapter = ItemListAdapter(
            ArrayList(),
            this
        )
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