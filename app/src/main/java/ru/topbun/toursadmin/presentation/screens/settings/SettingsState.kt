package ru.topbun.toursadmin.presentation.screens.settings

import io.ktor.util.date.GMTDate
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star
import ru.topbun.toursadmin.models.stars.Stars
import java.time.LocalDate

data class SettingsState(
    val city: City? = null,
    val maxDays: Int? = null,
    val countries: List<Country> = emptyList(),
    val regions: List<Region> = emptyList(),
    val operators: List<Operator> = emptyList(),
    val fromDate: GMTDate? = null,
    val toDate: GMTDate? = null,
    val star: Star? = null,
    val minRating: String? = null,
    val meal: Meal? = null,
    val delayUniquePosts: Int? = null,
    val delayPostingMinutes: Int? = null,
    val domain: String = "",

    val cityList: List<City> = emptyList(),
    val countryList: List<Country> = emptyList(),
    val regionList: List<Region> = emptyList(),
    val operatorList: List<Operator> = emptyList(),
    val starsList: List<Star> = emptyList(),
    val mealList: List<Meal> = emptyList(),

    val showChoiceCity: Boolean = false,
    val showChoiceStars: Boolean = false,
    val showChoiceMeal: Boolean = false,
    val showChoiceCountries: Boolean = false,
    val showChoiceRegions: Boolean = false,
    val showChoiceOperators: Boolean = false,
    val showChoiceFromDate: Boolean = false,
    val showChoiceToDate: Boolean = false,

    val screenState: SettingsScreenState = SettingsScreenState.Initial
){

    sealed interface SettingsScreenState{
        data object Initial: SettingsScreenState
        data object Loading: SettingsScreenState
        data object Error: SettingsScreenState
        data object Success: SettingsScreenState
    }

}
