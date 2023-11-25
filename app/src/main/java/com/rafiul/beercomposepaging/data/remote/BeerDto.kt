package com.rafiul.beercomposepaging.data.remote

import com.google.gson.annotations.SerializedName

data class BeerDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("first_brewed")
    val firstBrewed: String,
    @SerializedName("image_url")
    val imageUrl: String?
)
