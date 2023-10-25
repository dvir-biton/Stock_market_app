package com.fylora.stockmarket.core.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.fylora.stockmarket.data.csv.CSVParser
import com.fylora.stockmarket.data.csv.CompanyListingsParser
import com.fylora.stockmarket.data.csv.IntraDayInfoParser
import com.fylora.stockmarket.data.local.StockDatabase
import com.fylora.stockmarket.data.remote.StockApi
import com.fylora.stockmarket.data.repository.StockRepositoryImpl
import com.fylora.stockmarket.domain.model.CompanyListing
import com.fylora.stockmarket.domain.model.IntraDayInfo
import com.fylora.stockmarket.domain.repository.StockRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

interface AppModule {
    val stockApi: StockApi
    val stockDatabase: StockDatabase
    val listingParser: CSVParser<CompanyListing>
    val intraDayInfoParser: CSVParser<IntraDayInfo>
    val repository: StockRepository
}

@RequiresApi(Build.VERSION_CODES.O)
class AppModuleImpl(
    appContext: Context
): AppModule {
    override val stockApi: StockApi by lazy {
        Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    override val stockDatabase: StockDatabase by lazy {
        Room.databaseBuilder(
            context = appContext,
            klass = StockDatabase::class.java,
            name = "stock_db",
        ).build()
    }

    override val listingParser: CSVParser<CompanyListing> by lazy {
        CompanyListingsParser()
    }

    override val intraDayInfoParser: CSVParser<IntraDayInfo> by lazy {
        IntraDayInfoParser()
    }

    override val repository: StockRepository by lazy {
        StockRepositoryImpl(
            api = stockApi,
            db = stockDatabase,
            companyListingsParser = listingParser,
            intraDayInfoParser = intraDayInfoParser
        )
    }
}