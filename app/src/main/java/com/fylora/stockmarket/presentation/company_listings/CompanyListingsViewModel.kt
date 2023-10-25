package com.fylora.stockmarket.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fylora.stockmarket.domain.repository.StockRepository
import com.fylora.stockmarket.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompanyListingsViewModel (
    private val repository: StockRepository
): ViewModel(){

    var state by mutableStateOf(CompanyListingsState())
        private set

    private var searchJob: Job? = null

    init {
        getCompanyListings("", true)
    }

    fun onEvent(event: CompanyListingsEvent) {
        when(event) {
            is CompanyListingsEvent.OnSearchQueryChange -> {
                state = state.copy(
                    searchQuery = event.query
                )
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings(query = event.query)
                }
            }
            CompanyListingsEvent.Refresh -> getCompanyListings(fetchFromRemote = true)
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query).collect { result ->
                when(result) {
                    is Resource.Error -> Unit
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = result.isLoading
                        )
                    }
                    is Resource.Success -> {
                        result.data?.let { listings ->
                            state = state.copy(
                                companies = listings
                            )
                        }
                    }
                }
            }
        }
    }
}