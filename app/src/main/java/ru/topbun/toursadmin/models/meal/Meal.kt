package ru.topbun.toursadmin.models.meal

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val fullname: String,
    val id: String,
    val name: String,
    val russian: String,
    val russianfull: String
)