package ru.topbun.toursadmin.models.region

import kotlinx.serialization.Serializable

@Serializable
data class RegionLists(
    val regions: Regions
)