package com.example.mtgscannerproject.network

import com.example.mtgscannerproject.model.ScryfallCard
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryfallApi {

    @GET("cards/named")
    suspend fun getCardByName(
        @Query("fuzzy") cardName: String
    ): ScryfallCard
}