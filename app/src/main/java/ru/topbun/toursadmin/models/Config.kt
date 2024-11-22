package ru.topbun.toursadmin.models

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val city: Int,
    val maxDays: Int,
    val countries: String?,
    val regions: String?,
    val operators: String?,
    val dateFrom: String?,
    val dateTo: String?,
    val stars: Int?,
    val rating: Int?,
    val meal: Int?,
    val delayUniquePosts: Int,
    val delayPostingMinutes: Int,
)