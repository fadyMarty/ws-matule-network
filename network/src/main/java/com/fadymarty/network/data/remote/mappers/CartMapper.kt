package com.fadymarty.network.data.remote.mappers

import com.fadymarty.network.data.remote.dto.CartDto
import com.fadymarty.network.domain.model.Cart

fun CartDto.toCart(): Cart {
    return Cart(
        id = id,
        collectionId = collectionId,
        collectionName = collectionName,
        created = created,
        updated = updated,
        userId = userId,
        productId = productId,
        count = count
    )
}

fun Cart.toCartDto(): CartDto {
    return CartDto(
        id = id,
        collectionId = collectionId,
        collectionName = collectionName,
        created = created,
        updated = updated,
        userId = userId,
        productId = productId,
        count = count
    )
}