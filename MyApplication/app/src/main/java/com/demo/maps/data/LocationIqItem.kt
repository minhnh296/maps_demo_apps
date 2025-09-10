package com.demo.maps.data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationIqItem(
    @Json(name = "display_name") val display_name: String?,
    @Json(name = "lat") val lat: String?,
    @Json(name = "lon") val lon: String?
)