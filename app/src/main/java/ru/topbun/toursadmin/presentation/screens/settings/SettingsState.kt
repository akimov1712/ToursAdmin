package ru.topbun.toursadmin.presentation.screens.settings

import io.ktor.util.date.GMTDate
import ru.topbun.models.operatorToStock.OperatorToStock
import ru.topbun.toursadmin.models.city.City
import ru.topbun.toursadmin.models.config.Config
import ru.topbun.toursadmin.models.country.Country
import ru.topbun.toursadmin.models.meal.Meal
import ru.topbun.toursadmin.models.operator.Operator
import ru.topbun.toursadmin.models.region.Region
import ru.topbun.toursadmin.models.stars.Star
import ru.topbun.toursadmin.utills.formatDate
import ru.topbun.toursadmin.utills.parseToGTMDate

data class SettingsState(
    val configs: List<ConfigState> = emptyList(),
    val selectableConfigId: Int? = null,

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
    val showChoiceToStockOperator: Int? = null,

    val postingState: PostingScreenState = PostingScreenState.Initial,
    val screenState: SettingsScreenState = SettingsScreenState.Initial
){

    data class ConfigState(
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
        val delayUniquePosts: Int? = 7,
        val delayPostingMinutes: Int? = null,
        val domain: String = "https://tyrmarket.ru",
        val stocks: List<OperatorToStock> = emptyList(),
    ){

        companion object{
            fun fromConfig(config: Config) = ConfigState(
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
                stocks = config.stocks
            )
        }

        fun toConfig(): Config{
            val city = this.city ?: throw RuntimeException("Поле: \"Город вылета\" не может быть пустым")
            val rating = this.minRating?.toFloatOrNull()
            val delayUniquePosts = this.delayUniquePosts ?: throw RuntimeException("Поле: \"Задержка между уникальными постами в днях\" не может быть пустым")
            val delayPostingMinutes = this.delayPostingMinutes ?: throw RuntimeException("Поле: \"Задержка между постами в минутах\" не может быть пустым")
            val domain = this.domain.takeIf { it.isNotEmpty() } ?: throw RuntimeException("Поле: \"Домен сайта\" не может быть пустым")
            val stocks = this.stocks.filter { it.operator != null && it.stock.isNotEmpty() }
            return Config(
                city = city,
                maxDays = this.maxDays ?: 0,
                countries = this.countries,
                regions = this.regions,
                operators = this.operators,
                dateFrom = this.fromDate?.formatDate(),
                dateTo = this.toDate?.formatDate(),
                stars = this.star,
                rating = rating,
                meal = this.meal,
                delayUniquePosts = delayUniquePosts,
                delayPostingMinutes = delayPostingMinutes,
                domain = domain,
                stocks = stocks
            )
        }
    }

    sealed interface PostingScreenState{
        data object Initial: PostingScreenState
        data object Success: PostingScreenState
    }

    sealed interface SettingsScreenState{
        data object Initial: SettingsScreenState
        data object Loading: SettingsScreenState
        data class Error(val msg: String): SettingsScreenState
        data object Success: SettingsScreenState
    }

}
