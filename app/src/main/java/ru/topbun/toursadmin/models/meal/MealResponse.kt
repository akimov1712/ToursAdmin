package ru.topbun.toursadmin.models.meal

import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    val lists: MealLists
)