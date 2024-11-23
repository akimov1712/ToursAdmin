package ru.topbun.toursadmin.models.stars

import kotlinx.serialization.Serializable

@Serializable
data class StarsResponse(
    val lists: StarsLists
)