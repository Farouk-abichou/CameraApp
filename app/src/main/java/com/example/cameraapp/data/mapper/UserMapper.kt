package com.example.cameraapp.data.mapper

import com.example.cameraapp.domain.User
import com.example.cameraapp.data.local.User as LocalUser

class UserMapper {

    fun toDomain(user: LocalUser) = with(user) {
        User(
            usePinchToZoom = usePinchToZoom,
            useTapToFocus = useTapToFocus,
            useCamFront = useCamFront
        )
    }

    fun toLocal(user: User) = with(user) {
        LocalUser(
            usePinchToZoom = usePinchToZoom,
            useTapToFocus = useTapToFocus,
            useCamFront = useCamFront
        )
    }
}