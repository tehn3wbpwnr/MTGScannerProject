package com.example.mtgscannerproject.model

data class ScryfallCard (
    val name: String,
    val prices: Prices
    )

data class Prices(
    val usd: String?
)