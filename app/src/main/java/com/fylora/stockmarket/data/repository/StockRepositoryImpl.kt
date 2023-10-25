package com.fylora.stockmarket.data.repository

import com.fylora.stockmarket.data.csv.CSVParser
import com.fylora.stockmarket.data.local.StockDatabase
import com.fylora.stockmarket.data.mapper.toCompanyInfo
import com.fylora.stockmarket.data.mapper.toCompanyListing
import com.fylora.stockmarket.data.mapper.toCompanyListingEntity
import com.fylora.stockmarket.data.remote.StockApi
import com.fylora.stockmarket.domain.model.CompanyInfo
import com.fylora.stockmarket.domain.model.CompanyListing
import com.fylora.stockmarket.domain.model.IntraDayInfo
import com.fylora.stockmarket.domain.repository.StockRepository
import com.fylora.stockmarket.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class StockRepositoryImpl(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListings(query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )

                emit(Resource.Success(
                    data = dao.searchCompanyListings("").map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol = symbol)
            val results = intraDayInfoParser.parse(response.byteStream())

            Resource.Success(data = results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intraDay info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intraDay info")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val results = api.getCompanyInfo(symbol)
            Resource.Success(data = results.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info")
        }
    }
}