package com.fylora.stockmarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StockDao {

    @Upsert
    suspend fun insertCompanyListings(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companies")
    suspend fun clearCompanyListings()

    @Query(
        """
            SELECT * FROM companies
            WHERE LOWER(name) Like '%' || LOWER(:query) || '%' OR :query == symbol
        """
    )
    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity>
}