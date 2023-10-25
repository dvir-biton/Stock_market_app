package com.fylora.stockmarket.presentation.company_info.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fylora.stockmarket.StockApplication
import com.fylora.stockmarket.presentation.company_info.CompanyInfoViewModel
import com.fylora.stockmarket.presentation.viewModelFactory
import com.fylora.stockmarket.ui.theme.DarkGray
import com.ramcosta.composedestinations.annotation.Destination

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Destination
fun CompanyInfoScreen(
    symbol: String
) {
    val viewModel = viewModel<CompanyInfoViewModel>(
        factory = viewModelFactory {
            CompanyInfoViewModel(
                savedStateHandler = SavedStateHandle(initialState = mapOf("symbol" to symbol)),
                repository = StockApplication.appModule.repository
            )
        }
    )
    val state = viewModel.state

    if(state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            state.company?.let { company ->
                Text(
                    text = company.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = company.symbol,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Industry: ${company.industry}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Country: ${company.country}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = company.description,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                if(state.stockInfos.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Market Summary",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    StockChart(
                        infos = state.stockInfos,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(state.isLoading){
            CircularProgressIndicator()
        } else if(state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}