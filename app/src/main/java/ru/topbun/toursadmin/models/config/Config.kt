package ru.topbun.toursadmin.models.config

import kotlinx.serialization.Serializable
import ru.topbun.models.operatorToStock.OperatorToStock
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star

@Serializable
data class Config(
    val title: String,
    val city: City?,
    val maxDays: Int,
    val countries: List<Country>,
    val regions: List<Region>,
    val operators: List<Operator>,
    val dateFrom: String?,
    val dateTo: String?,
    val stars: Star?,
    val rating: Float?,
    val meal: Meal?,
    val delayUniquePosts: Int,
    val delayPostingMinutes: Int,
    val domain: String,
    val stocks: List<OperatorToStock>
)