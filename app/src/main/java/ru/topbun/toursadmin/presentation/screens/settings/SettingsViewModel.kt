package ru.topbun.toursadmin.presentation.screens.settings

import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.launch
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.country.Countries
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star
import ru.topbun.toursadmin.network.TourvisorApi
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.SettingsScreenState
import ru.topbun.toursadmin.repository.TourvisorRepository
import ru.topbun.toursadmin.utills.ScreenModelState

class SettingsViewModel : ScreenModelState<SettingsState>(SettingsState()) {

    private val api = TourvisorApi()
    private val repository = TourvisorRepository(api)

    fun changeMaxDays(value: String) {
        val maxDays = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateState { copy(maxDays = maxDays) }
    }

    fun changeDelayPostingMinutes(value: String) {
        val delay = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateState { copy(delayPostingMinutes = delay) }
    }

    fun changeDelayUniquePosts(value: String) {
        val delay = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateState { copy(delayUniquePosts = delay) }
    }

    fun changeRating(value: String) {
        updateState { copy(minRating = value) }
    }

    fun changeDomain(domain: String) = updateState { copy(domain = domain) }
    fun changeCity(city: City?) = updateState { copy(city = city) }
    fun changeStars(star: Star?) = updateState { copy(star = star) }
    fun changeMeal(meal: Meal?) = updateState { copy(meal = meal) }
    fun changeCountries(countries: List<Country>) = updateState { copy(countries = countries) }
    fun changeRegions(regions: List<Region>) = updateState { copy(regions = regions) }
    fun changeOperators(operators: List<Operator>) = updateState { copy(operators = operators) }
    fun changeFromDate(date: GMTDate?) = updateState { copy(fromDate = date) }
    fun changeToDate(date: GMTDate?) = updateState { copy(toDate = date) }

    fun showChoiceCity(value: Boolean) = updateState { copy(showChoiceCity = value) }
    fun showChoiceStars(value: Boolean) = updateState { copy(showChoiceStars = value) }
    fun showChoiceMeal(value: Boolean) = updateState { copy(showChoiceMeal = value) }
    fun showChoiceCountry(value: Boolean) = updateState { copy(showChoiceCountries = value) }
    fun showChoiceRegion(value: Boolean) = updateState { copy(showChoiceRegions = value) }
    fun showChoiceOperator(value: Boolean) = updateState { copy(showChoiceOperators = value) }
    fun showChoiceFromDate(value: Boolean) = updateState { copy(showChoiceFromDate = value) }
    fun showChoiceToDate(value: Boolean) = updateState { copy(showChoiceToDate = value) }

    private fun initialLoading() = screenModelScope.launch {
        try {
            updateState { copy(screenState = SettingsScreenState.Loading) }
            val cities = repository.getDeparture()
            val meals = repository.getMeal()
            val region = repository.getRegion()
            val country = repository.getCountry()
            val operators = repository.getOperator()
            val stars = repository.getStars()
            updateState {
                copy(
                    cityList = cities,
                    mealList = meals,
                    regionList = region,
                    countryList = country,
                    operatorList = operators,
                    starsList = stars,
                    screenState = SettingsScreenState.Success
                )
            }
            println(stateValue)
        } catch (e: Exception) {
            e.printStackTrace()
            updateState { copy(screenState = SettingsScreenState.Error) }
        }
    }

    init {
        initialLoading()
    }

}