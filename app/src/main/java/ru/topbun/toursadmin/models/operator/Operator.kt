package ru.topbun.toursadmin.models.operator

import kotlinx.serialization.Serializable

@Serializable
data class Operator(
    val fullname: String,
    val id: String,
    val name: String,
    val russian: String
)