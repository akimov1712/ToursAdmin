package ru.topbun.toursadmin.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.ConfigState.Companion.fromConfig
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

    fun changeSelectableConfigId(id: Int?) = updateState {copy(selectableConfigId = id) }

    fun addConfig() = updateState {
        copy(configs = configs + SettingsState.ConfigState())
    }

    fun deleteConfig() = updateState {
        val newConfigs = configs.filterIndexed { index, _ -> index != selectableConfigId }
        if (newConfigs.isNotEmpty()){
            copy(configs = newConfigs, selectableConfigId = null)
        } else {
            copy(screenState = SettingsScreenState.Error("Должен быть хотя-бы один фильтр"))
        }
    }

    fun changeStockText(text: String, index: Int){
        val config = getConfigWithSelectable()
        val newList = config.stocks.mapIndexed { i, stock ->
            if (index == i) stock.copy(stock = text) else stock
        }
         updateNewConfig(config.copy(stocks = newList))
    }

    fun changeStockOperator(operator: Operator?, index: Int){
        val config = getConfigWithSelectable()
        val newList = config.stocks.mapIndexed { i, stock ->
            if (index == i) stock.copy(operator = operator) else stock
        }
        updateNewConfig(config.copy(stocks = newList))
    }

    fun addStock() {
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(stocks = config.stocks.toList() + OperatorToStock(null, "")))
    }

    fun removeStock(index: Int) {
        val config = getConfigWithSelectable()
        val stocks = config.stocks.toMutableList().apply {
            removeAt(index)
        }
        updateNewConfig(config.copy(stocks = stocks))
    }

    fun setConfig() = screenModelScope.launch {
        try {
            updateState { copy(screenState = SettingsScreenState.Loading) }
            val configs = stateValue.configs.map {
                it.toConfig()
            }
            postingRepository.setConfig(configs)
            updateState { copy(
                screenState = SettingsScreenState.Success,
                postingState = Success
            ) }
        } catch (e: Exception) {
            e.printStackTrace()
            updateState { copy(screenState = SettingsScreenState.Error(e.message ?: "Произошла ошибка")) }
        }
    }

    fun resetScreenState() = updateState { copy(screenState = SettingsScreenState.Initial) }
    fun resetPostingState() = updateState { copy(postingState = SettingsState.PostingScreenState.Initial) }

    fun getConfigWithSelectable() = stateValue.selectableConfigId?.let { stateValue.configs[it] } ?: throw RuntimeException("editable config not selected")

    private fun updateNewConfig(config: SettingsState.ConfigState) = updateState {
        val updateConfigs = configs.mapIndexed { index, conf ->
            if (index == selectableConfigId) config else conf
        }
        copy(configs = updateConfigs)
    }

    private fun loadConfig() = screenModelScope.launch {
        val config = postingRepository.getConfig()
        val configStates = config.map { fromConfig(it) }
        updateState { copy(configStates) }
    }

    fun changeMaxDays(value: String) {
        val config = getConfigWithSelectable()
        val maxDays = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateNewConfig(config.copy(maxDays = maxDays))
    }

    fun changeDelayPostingMinutes(value: String) {
        val config = getConfigWithSelectable()
        val delay = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateNewConfig(config.copy(delayPostingMinutes = delay))
    }

    fun changeDelayUniquePosts(value: String) {
        val config = getConfigWithSelectable()
        val delay = value.toIntOrNull() ?: run { if (value.isEmpty()) null else return }
        updateNewConfig(config.copy(delayUniquePosts = delay))
    }

    fun changeRating(value: String) {
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(minRating = value))
    }

    fun changeDomain(domain: String){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(domain = domain))
    }

    fun changeCity(city: City?){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(city = city))
    }

    fun changeStars(star: Star?){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(star = star))
    }

    fun changeMeal(meal: Meal?){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(meal = meal))
    }

    fun changeCountries(countries: List<Country>){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(countries = countries))
    }

    fun changeRegions(regions: List<Region>){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(regions = regions))
    }

    fun changeOperators(operators: List<Operator>){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(operators = operators))
    }

    fun changeFromDate(date: GMTDate?){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(fromDate = date))
    }

    fun changeToDate(date: GMTDate?){
        val config = getConfigWithSelectable()
        updateNewConfig(config.copy(toDate = date))
    }


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

@Composable
fun SettingsState.stateConfig(): SettingsState.ConfigState? {
    return this.selectableConfigId?.let { this.configs.getOrNull(it) }
}