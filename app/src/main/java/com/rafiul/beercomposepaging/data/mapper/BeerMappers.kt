package com.rafiul.beercomposepaging.data.mapper

import com.rafiul.beercomposepaging.data.local.BeerEntity
import com.rafiul.beercomposepaging.data.remote.BeerDto
import com.rafiul.beercomposepaging.domain.Beer


fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name = name,
        tagline = tagline,
        description = description,
        firstBrewed = firstBrewed,
        imageUrl = imageUrl ?: " "
    )
}

fun BeerEntity.toBeer(): Beer {
    return Beer(
        id = id,
        name = name,
        tagline = tagline,
        description = description,
        firstBrewed = firstBrewed,
        imageUrl = imageUrl
    )
}