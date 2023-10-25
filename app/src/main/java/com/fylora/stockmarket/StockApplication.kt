package com.fylora.stockmarket

import android.app.Application
import com.fylora.stockmarket.core.di.AppModule
import com.fylora.stockmarket.core.di.AppModuleImpl

class StockApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }

    companion object {
        lateinit var appModule: AppModule
    }
}