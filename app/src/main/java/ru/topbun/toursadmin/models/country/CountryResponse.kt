package ru.topbun.toursadmin.models.country

import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    val lists: CountryLists
)