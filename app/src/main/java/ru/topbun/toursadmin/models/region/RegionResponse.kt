package ru.topbun.toursadmin.models.region

import kotlinx.serialization.Serializable

@Serializable
data class RegionResponse(
    val lists: RegionLists
)