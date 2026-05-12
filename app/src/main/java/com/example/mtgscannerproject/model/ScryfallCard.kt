package com.example.mtgscannerproject.model

data class ScryfallCard(
    val name: String,
    val prices: Prices?,
    val image_uris: ImageUris?
)

data class Prices(
    val usd: String?
)

data class ImageUris(
    val normal: String?
)