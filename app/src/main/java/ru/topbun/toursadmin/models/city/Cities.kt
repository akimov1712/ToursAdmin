package ru.topbun.toursadmin.models.city

import kotlinx.serialization.Serializable

@Serializable
data class Cities(
    val departure: List<City>
)