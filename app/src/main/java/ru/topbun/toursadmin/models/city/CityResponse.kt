package ru.topbun.toursadmin.models.city

import kotlinx.serialization.Serializable
import ru.topbun.toursadmin.models.country.CountryLists

@Serializable
data class CityResponse(
    val lists: CityList
)