package ru.topbun.toursadmin.models.country

import kotlinx.serialization.Serializable

@Serializable
data class Countries(
    val country: List<Country>
)