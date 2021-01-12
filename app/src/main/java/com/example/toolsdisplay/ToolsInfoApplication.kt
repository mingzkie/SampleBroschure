package com.example.toolsdisplay

import android.app.Application
import com.example.githubuser.network.ConnectivityInterceptor
import com.example.githubuser.network.ConnectivityInterceptorImpl
import com.example.toolsdisplay.database.ToolsRoomDatabase
import com.example.toolsdisplay.detailscreen.views.DetailViewModelFactory
import com.example.toolsdisplay.repository.ToolsInfoRepository
import com.example.toolsdisplay.repository.ToolsInfoRepositoryImpl
import com.example.toolsdisplay.home.views.HomeViewModelFactory
import com.example.toolsdisplay.login.repository.LoginRepository
import com.example.toolsdisplay.login.repository.LoginRepositoryImpl
import com.example.toolsdisplay.login.views.LoginViewModelFactory
import com.example.toolsdisplay.service.Service
import com.example.toolsdisplay.service.ServiceDataSource
import com.example.toolsdisplay.service.ServiceDataSourceImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ToolsInfoApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@ToolsInfoApplication))

        bind() from singleton { ToolsRoomDatabase(instance()) }
        bind() from singleton { instance<ToolsRoomDatabase>().toolsDao() }
        bind<ConnectivityInterceptor>()  with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { Service(instance()) }
        bind<ServiceDataSource>() with singleton { ServiceDataSourceImpl(instance()) }
        bind<LoginRepository>() with singleton { LoginRepositoryImpl(instance(), instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
        bind<ToolsInfoRepository>() with singleton { ToolsInfoRepositoryImpl(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { DetailViewModelFactory(instance()) }

    }
}