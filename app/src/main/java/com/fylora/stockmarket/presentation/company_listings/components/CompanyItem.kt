package com.fylora.stockmarket.presentation.company_listings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fylora.stockmarket.domain.model.CompanyListing

@Composable
fun CompanyItem(
    company: CompanyListing,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.weight(1f)
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = company.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = company.exchange,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "(${company.symbol})",
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Text(text = "")
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyItemPrev() {
    CompanyItem(
        company = CompanyListing(
            name = "test",
            symbol = "Test",
            exchange = "TE"
        )
    )
}