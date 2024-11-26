package ru.topbun.models.operatorToStock

import kotlinx.serialization.Serializable
import ru.topbun.toursadmin.models.operator.Operator

@Serializable
data class OperatorToStock(
    val operator: Operator?,
    val stock: String
)
