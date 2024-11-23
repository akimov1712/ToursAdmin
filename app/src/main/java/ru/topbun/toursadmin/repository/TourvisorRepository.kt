package ru.topbun.toursadmin.repository

import io.ktor.client.call.body
import ru.topbun.toursadmin.models.TypeContent
import ru.topbun.toursadmin.models.TypeContent.country
import ru.topbun.toursadmin.models.TypeContent.meal
import ru.topbun.toursadmin.models.TypeContent.operator
import ru.topbun.toursadmin.models.TypeContent.region
import ru.topbun.toursadmin.models.TypeContent.stars
import ru.topbun.toursadmin.models.country.CountryResponse
import ru.topbun.toursadmin.models.meal.MealResponse
import ru.topbun.toursadmin.models.operator.OperatorResponse
import ru.topbun.toursadmin.models.region.RegionResponse
import ru.topbun.toursadmin.models.stars.StarsResponse
import ru.topbun.toursadmin.network.TourvisorApi

class TourvisorRepository(private val api: TourvisorApi) {

    suspend fun getCountry() = api.getContent(country).body<CountryResponse>().lists.countries.country
    suspend fun getRegion() = api.getContent(region).body<RegionResponse>().lists.regions.region
    suspend fun getOperator() = api.getContent(operator).body<OperatorResponse>().lists.operators.operator
    suspend fun getStart() = api.getContent(stars).body<StarsResponse>().lists.stars.star
    suspend fun getMeal() = api.getContent(meal).body<MealResponse>().lists.meals.meal

}