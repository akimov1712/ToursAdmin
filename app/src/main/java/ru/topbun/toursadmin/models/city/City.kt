package ru.topbun.toursadmin.models.city

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: String,
    val name: String,
    val namefrom: String,
)