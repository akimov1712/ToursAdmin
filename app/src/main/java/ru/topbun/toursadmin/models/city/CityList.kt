package ru.topbun.toursadmin.models.city

import kotlinx.serialization.Serializable
import ru.topbun.toursadmin.models.country.Countries

@Serializable
data class CityList(
    val departures: Cities
)