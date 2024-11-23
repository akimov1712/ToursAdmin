package ru.topbun.toursadmin.models.meal

import kotlinx.serialization.Serializable

@Serializable
data class Meals(
    val meal: List<Meal>
)