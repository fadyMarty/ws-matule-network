package com.fadymarty.network.data.remote.mappers

import com.fadymarty.network.data.remote.dto.AuthResponseDto
import com.fadymarty.network.domain.model.AuthResponse

fun AuthResponseDto.toAuthResponse(): AuthResponse {
    return AuthResponse(
        record = record.toUser(),
        token = token
    )
}

fun AuthResponse.toAuthResponseDto(): AuthResponseDto {
    return AuthResponseDto(
        record = record.toUserDto(),
        token = token
    )
}