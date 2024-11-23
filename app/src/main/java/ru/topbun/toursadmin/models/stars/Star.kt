package ru.topbun.toursadmin.models.stars

import kotlinx.serialization.Serializable

@Serializable
data class Star(
    val id: String,
    val name: String
)