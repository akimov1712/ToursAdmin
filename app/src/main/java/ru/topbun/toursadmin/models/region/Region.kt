package ru.topbun.toursadmin.models.region

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val country: String,
    val id: String,
    val name: String
)