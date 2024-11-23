package ru.topbun.toursadmin.models.region

import kotlinx.serialization.Serializable

@Serializable
data class Regions(
    val region: List<Region>
)