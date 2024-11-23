package ru.topbun.toursadmin.models.country

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: String,
    val name: String
)