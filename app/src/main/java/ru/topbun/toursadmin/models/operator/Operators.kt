package ru.topbun.toursadmin.models.operator

import kotlinx.serialization.Serializable

@Serializable
data class Operators(
    val `operator`: List<Operator>
)