package com.fylora.stockmarket.presentation.company_info

import com.fylora.stockmarket.domain.model.CompanyInfo
import com.fylora.stockmarket.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfos: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
