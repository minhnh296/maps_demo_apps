package com.demo.maps.api

import com.demo.maps.data.LocationIqItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("v1/search")
    suspend fun search(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10
    ): List<LocationIqItem>

    companion object {
        fun create(baseUrl: String = "https://us1.locationiq.com/"): Services {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                })
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit.create(Services::class.java)
        }
    }
}

