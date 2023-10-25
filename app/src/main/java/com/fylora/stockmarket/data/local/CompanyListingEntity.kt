package com.fylora.stockmarket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyListingEntity(
    val name: String,
    val symbol: String,
    val exchange: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
