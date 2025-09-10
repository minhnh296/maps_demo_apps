package com.demo.maps.data

import android.util.Log
import com.demo.maps.api.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val locationIqService: Services,
    private val locationIqKey: String
) {
    suspend fun searchAddress(query: String): List<SearchResult> = withContext(Dispatchers.IO) {
        try {
            val list = locationIqService.search(q = query, key = locationIqKey)
            Log.d("Repository", "RAW response list size: ${list.size}, data: $list")

            list.map {
                SearchResult(
                    title = it.display_name ?: "",
                    subtitle = it.display_name ?: "",
                    lat = it.lat?.toDoubleOrNull(),
                    lng = it.lon?.toDoubleOrNull()
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching data", e)
            emptyList()
        }
    }
}
