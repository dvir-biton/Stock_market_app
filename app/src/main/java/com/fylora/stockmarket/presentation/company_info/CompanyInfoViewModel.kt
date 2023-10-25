package com.fylora.stockmarket.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fylora.stockmarket.domain.repository.StockRepository
import com.fylora.stockmarket.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CompanyInfoViewModel(
    private val savedStateHandler: SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())
        private set

    init {
        viewModelScope.launch {
            val symbol = savedStateHandler.get<String>("symbol") ?: return@launch

            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intraDayInfoResult = async { repository.getIntraDayInfo(symbol) }

            state = when(val result = companyInfoResult.await()) {
                is Resource.Success -> state.copy(
                    company = result.data,
                    isLoading = false
                )
                is Resource.Error -> state.copy(
                    error = result.message,
                    isLoading = false
                )
                else -> state.copy(
                    isLoading = false
                )
            }

            state = when(val result = intraDayInfoResult.await()) {
                is Resource.Success -> state.copy(
                    stockInfos = result.data ?: emptyList(),
                    isLoading = false
                )
                is Resource.Error -> state.copy(
                    error = result.message,
                    isLoading = false
                )
                else -> state.copy(
                    isLoading = false
                )
            }
        }
    }
}