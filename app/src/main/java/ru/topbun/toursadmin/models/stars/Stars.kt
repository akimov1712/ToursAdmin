package ru.topbun.toursadmin.models.stars

import kotlinx.serialization.Serializable

@Serializable
data class Stars(
    val star: List<Star>
)