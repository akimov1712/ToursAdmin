package ru.topbun.toursadmin.models.operator

import kotlinx.serialization.Serializable

@Serializable
data class OperatorResponse(
    val lists: OperatorLists
)