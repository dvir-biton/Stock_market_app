package com.fylora.stockmarket.presentation.company_listings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fylora.stockmarket.StockApplication
import com.fylora.stockmarket.presentation.company_listings.CompanyListingsEvent
import com.fylora.stockmarket.presentation.company_listings.CompanyListingsViewModel
import com.fylora.stockmarket.presentation.destinations.CompanyInfoScreenDestination
import com.fylora.stockmarket.presentation.viewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination(start = true)
fun CompanyListingScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = viewModel<CompanyListingsViewModel>(
        factory = viewModelFactory {
            CompanyListingsViewModel(repository = StockApplication.appModule.repository)
        }
    )
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                viewModel.onEvent(CompanyListingsEvent.OnSearchQueryChange(query))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search")
            },
            singleLine = true
        )
        
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(CompanyListingsEvent.Refresh)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.companies.size) { i ->
                    val company = state.companies[i]

                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(company.symbol)
                                )
                            }
                            .padding(16.dp)
                    )
                    if(i < state.companies.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}