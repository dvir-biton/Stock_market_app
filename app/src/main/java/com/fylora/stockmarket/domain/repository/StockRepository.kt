package com.fylora.stockmarket.domain.repository

import com.fylora.stockmarket.domain.model.CompanyInfo
import com.fylora.stockmarket.domain.model.CompanyListing
import com.fylora.stockmarket.domain.model.IntraDayInfo
import com.fylora.stockmarket.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,

    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol: String,
    ): Resource<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String,
    ): Resource<CompanyInfo>
}