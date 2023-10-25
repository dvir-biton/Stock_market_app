package com.fylora.stockmarket.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.fylora.stockmarket.data.mapper.toIntraDayInfo
import com.fylora.stockmarket.data.remote.dto.IntraDayInfoDto
import com.fylora.stockmarket.domain.model.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class IntraDayInfoParser: CSVParser<IntraDayInfo> {
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null

                    val dto = IntraDayInfoDto(
                        timestamp = timestamp,
                        close = close.toDouble()
                    )

                    dto.toIntraDayInfo()
                }
                .filter {
                    if(LocalDate.now().dayOfWeek == DayOfWeek.MONDAY ||
                        LocalDate.now().dayOfWeek == DayOfWeek.SUNDAY ||
                        LocalDate.now().dayOfWeek == DayOfWeek.SATURDAY) {
                        it.date.dayOfMonth == LocalDate.now().minusDays(4).dayOfMonth
                    } else {
                        it.date.dayOfMonth == LocalDate.now().minusDays(1).dayOfMonth
                    }
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}