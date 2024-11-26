package ru.topbun.toursadmin.presentation.screens.settings

import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.launch
import ru.topbun.models.operatorToStock.OperatorToStock
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.config.Config
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star
import ru.topbun.toursadmin.network.PostingApi
import ru.topbun.toursadmin.network.TourvisorApi
import ru.topbun.toursadmin.presentation.screens.login.LoginState
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.PostingScreenState.Success
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.SettingsScreenState
import ru.topbun.toursadmin.repository.PostingRepository
import ru.topbun.toursadmin.repository.TourvisorRepository
import ru.topbun.toursadmin.utills.ScreenModelState
import ru.topbun.toursadmin.utills.formatDate
import ru.topbun.toursadmin.utills.parseToGTMDate

class SettingsViewModel : ScreenModelState<SettingsState>(SettingsState()) {

    private val postingApi = PostingApi()
    private val tourvisorApi = TourvisorApi()
    private val tourvisorRepository = TourvisorRepository(tourvisorApi)
    private val postingRepository = PostingRepository(postingApi)

    fun changeStockText(text: String, index: Int) = updateState {
        val newList = stocks.mapIndexed { i, stock ->
            if (index == i) stock.copy(stock = text) else stock
        }
        copy(stocks = newList)
    }

    fun changeStockOperator(operator: Operator?, index: Int) = updateState {
        val newList = stocks.mapIndexed { i, stock ->
            if (index == i) stock.copy(operator = operator) else stock
        }
        copy(stocks = newList)
    }

    fun addStock() = updateState { copy(stocks = stateValue.stocks.toList() + OperatorToStock(null, "")) }

    fun removeStock(index: Int) = updateState {
        val stocks = stateValue.stocks.toMutableList().apply {
            removeAt(index)
        }
        copy(stocks = stocks)
    }

    fun setConfig() = screenModelScope.launch {
        try {
            updateState { copy(screenState = SettingsScreenState.Loading) }
            val city = stateValue.city ?: throw RuntimeException("Поле: \"Город вылета\" не может быть пустым")
            val rating = stateValue.minRating?.toFloatOrNull() // TODO питание не работает
            val delayUniquePosts = stateValue.delayUniquePosts ?: throw RuntimeException("Поле: \"Задержка между уникальными постами в днях\" не может быть пустым")
            val delayPostingMinutes = stateValue.delayPostingMinutes ?: throw RuntimeException("Поле: \"Задержка между постами в минутах\" не может быть пустым")
            val domain = stateValue.domain.takeIf { it.isNotEmpty() } ?: throw RuntimeException("Поле: \"Домен сайта\" не может быть пустым")
            val stocks = stateValue.stocks.filter { it.operator != null && it.stock.isNotEmpty() }
            val config = Config(
                city = city,
                maxDays = stateValue.maxDays ?: 0,
                countries = stateValue.countries,
                regions = stateValue.regions,
                operators = stateValue.operators,
                dateFrom = stateValue.fromDate?.formatDate(),
                dateTo = stateValue.toDate?.formatDate(),
                stars = stateValue.star,
                rating = rating,
                meal = stateValue.meal,
                delayUniquePosts = delayUniquePosts,
                delayPostingMinutes = delayPostingMinutes,
                domain = domain,
                stocks = stocks
            )
            postingRepository.setConfig(config)
            updateState { copy(
                screenState = SettingsScreenState.Success,
                postingState = Success
            ) }
            println(stateValue)
        } catch (e: Exception) {
            e.printStackTrace()
            updateState { copy(screenState = SettingsScreenState.Error(e.message ?: "Произошла ошибка")) }
        }
    }

    fun resetScreenState() = updateState { copy(screenState = SettingsScreenState.Initial) }
    fun resetPostingState() = updateState { copy(postingState = SettingsState.PostingScreenState.Initial) }

    private fun loadConfig() = screenModelScope.launch {
        val config = postingRepository.getConfig()
        updateState {
            copy(
                city = config.city,
                maxDays = config.maxDays,
                countries = config.countries,
                regions = config.regions,
                operators = config.operators,
                fromDate = config.dateFrom?.parseToGTMDate(),
                toDate = config.dateTo?.parseToGTMDate(),
                star = config.stars,
                minRating = config.rating?.toString(),
                meal = config.meal,
                delayUniquePosts = config.delayUniquePosts,
                delayPostingMinutes = config.delayPostingMinutes,
                domain = config.domain,
            )
        }
    }

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
    fun showChoiceToStackOperator(index: Int?) = updateState { copy(showChoiceToStockOperator = index) }

    private fun initialLoading() = screenModelScope.launch {
        try {
            updateState { copy(screenState = SettingsScreenState.Loading) }
            val cities = tourvisorRepository.getDeparture()
            val meals = tourvisorRepository.getMeal()
            val region = tourvisorRepository.getRegion()
            val country = tourvisorRepository.getCountry()
            val operators = tourvisorRepository.getOperator()
            val stars = tourvisorRepository.getStars()
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
            updateState { copy(screenState = SettingsScreenState.Error(e.message ?: "Произошла ошибка")) }
        }
    }

    init {
        loadConfig()
        initialLoading()
    }

}