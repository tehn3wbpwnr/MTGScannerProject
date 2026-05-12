package com.example.mtgscannerproject.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.scryfall.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val headerInterceptor =
        okhttp3.Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "User-Agent",
                    "MTGScannerProject/1.0"
                )
                .addHeader(
                    "Accept",
                    "application/json"
                )
                .build()
            chain.proceed(request)
        }
    private val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ScryfallApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScryfallApi::class.java)
    }
}