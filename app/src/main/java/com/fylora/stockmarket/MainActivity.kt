package com.fylora.stockmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fylora.stockmarket.presentation.NavGraphs
import com.fylora.stockmarket.ui.theme.StockMarketTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockMarketTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root
                )
            }
        }
    }
}
