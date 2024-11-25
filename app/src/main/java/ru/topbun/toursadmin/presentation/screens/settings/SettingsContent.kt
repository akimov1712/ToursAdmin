package ru.topbun.toursadmin.presentation.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import ru.topbun.toursadmin.presentation.theme.Colors
import ru.topbun.toursadmin.utills.formatDate
import java.time.Clock

object SettingsScreen: Screen{

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val viewModel = rememberScreenModel { SettingsViewModel() }
            val state by viewModel.state.collectAsState()

            TitleWithButton("Город вылета", state.city?.name){ viewModel.showChoiceCity(true) }
            TitleWithTextField(
                title = "Ограничить дату вылета, на ближайшие дни",
                placeholder = "Количество дней",
                text = state.maxDays?.toString() ?: ""
            ){ if(it.length < 4) viewModel.changeMaxDays(it)}
            val countriesTextButton = state.countries.joinToString { it.name }.takeIf { state.countries.isNotEmpty() }
            TitleWithButton("Страны", countriesTextButton){ viewModel.showChoiceCountry(true) }

            val regionTextButton = state.regions.joinToString { it.name }.takeIf { state.regions.isNotEmpty() }
            TitleWithButton("Регионы", regionTextButton){ viewModel.showChoiceRegion(true) }

            val operatorTextButton = state.operators.joinToString { it.name }.takeIf { state.operators.isNotEmpty() }
            TitleWithButton("Туроператоры", operatorTextButton){ viewModel.showChoiceOperator(true) }

            TitleWithButton(
                title = "С даты",
                textButton = state.fromDate?.formatDate(),
                onCancel = state.fromDate?.let{ { viewModel.changeFromDate(null) }}
            ){ viewModel.showChoiceFromDate(true) }

            TitleWithButton(
                title = "По дату",
                textButton = state.toDate?.formatDate(),
                onCancel = state.toDate?.let{ { viewModel.changeToDate(null) }}
            ){ viewModel.showChoiceToDate(true) }

            TitleWithButton("Мин. звездность отелей", state.star?.name){ viewModel.showChoiceStars(true) }
            TitleWithTextField(
                title = "Мин. рейтинг отелей",
                placeholder = "Рейтинг отелей",
                text = state.minRating ?: "",
                keyboardType = KeyboardType.Decimal
            ){ if(it.length < 4) viewModel.changeRating(it) }
            TitleWithButton("Тип питания", state.meal?.russian){ viewModel.showChoiceMeal(true) }
            TitleWithTextField(
                title = "Задержка между уникальными постами в днях",
                placeholder = "Количество дней",
                text = state.delayUniquePosts?.toString() ?: ""
            ){ if(it.length < 4) viewModel.changeDelayUniquePosts(it) }
            TitleWithTextField(
                title = "Задержка между постами в минутах",
                placeholder = "Количество минут",
                text = state.delayPostingMinutes?.toString() ?: ""
            ){ if(it.length < 4) viewModel.changeDelayPostingMinutes(it) }
            TitleWithTextField(
                title = "Домен сайта",
                placeholder = "Введите домен",
                text = state.domain,
                keyboardType = KeyboardType.Text
            ){ viewModel.changeDomain(it) }
            SetPickDialogs(viewModel = viewModel)
        }
    }
}

@Composable
private fun SetPickDialogs(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsState()
    ChoiceCityDialog(viewModel, state)
    ChoiceStarsDialog(viewModel, state)
    ChoiceMealDialog(viewModel, state)
    ChoiceCountryDialog(viewModel, state)
    ChoiceRegionDialog(viewModel, state)
    ChoiceOperatorDialog(viewModel, state)
    ChoiceFromDateDialog(viewModel, state)
    ChoiceToDateDialog(viewModel, state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceFromDateDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceFromDate){
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DialogDatePicker(
            datePickerState,
            onDismissRequest = { viewModel.showChoiceFromDate(false) },
            onConfirm = { viewModel.changeFromDate(it); viewModel.showChoiceFromDate(false) },
            onCancel = { viewModel.showChoiceFromDate(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceToDateDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceToDate){
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DialogDatePicker(
            datePickerState,
            onDismissRequest = { viewModel.showChoiceToDate(false) },
            onConfirm = { viewModel.changeToDate(it) ; viewModel.showChoiceToDate(false) },
            onCancel = { viewModel.showChoiceToDate(false) }
        )
    }
}

@Composable
private fun ChoiceOperatorDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceOperators && state.operatorList.isNotEmpty()){
        MultipleChoiceBottomSheet(
            title = "Туроператоры",
            items = state.operatorList.map { it.name },
            selectedItems = state.operators.map { it.name },
            onDismiss = { viewModel.showChoiceOperator(false) }
        ){
            val operators = it.map { state.operatorList[it] }
            viewModel.changeOperators(operators)
            viewModel.showChoiceOperator(false)
        }
    }
}


@Composable
private fun ChoiceRegionDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceRegions && state.regionList.isNotEmpty()){
        MultipleChoiceBottomSheet(
            title = "Регионы",
            items = state.regionList.map { it.name },
            selectedItems = state.regions.map { it.name },
            onDismiss = { viewModel.showChoiceRegion(false) }
        ){
            val regions = it.map { state.regionList[it] }
            viewModel.changeRegions(regions)
            viewModel.showChoiceRegion(false)
        }
    }
}

@Composable
private fun ChoiceCountryDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceCountries && state.countryList.isNotEmpty()){
        MultipleChoiceBottomSheet(
            title = "Страны",
            items = state.countryList.map { it.name },
            selectedItems = state.countries.map { it.name },
            onDismiss = { viewModel.showChoiceCountry(false) }
        ){
            val countries = it.map { state.countryList[it] }
            viewModel.changeCountries(countries)
            viewModel.showChoiceCountry(false)
        }
    }
}

@Composable
private fun ChoiceCityDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceCity && state.cityList.isNotEmpty()){
        SingleChoiceBottomSheet(
            title = "Город вылета",
            items = state.cityList.map { it.name },
            selectedItem = state.city?.name,
            onDismiss = { viewModel.showChoiceCity(false) }
        ){
            val city = it?.let { state.cityList[it] }
            viewModel.changeCity(city)
            viewModel.showChoiceCity(false)
        }
    }
}

@Composable
private fun ChoiceMealDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceMeal && state.mealList.isNotEmpty()){
        SingleChoiceBottomSheet(
            title = "Тип питания",
            items = state.mealList.map { it.russian + " (${it.name})" },
            selectedItem = state.meal?.name,
            onDismiss = { viewModel.showChoiceMeal(false) }
        ){
            val meal = it?.let { state.mealList[it] }
            viewModel.changeMeal(meal)
            viewModel.showChoiceMeal(false)
        }
    }
}

@Composable
private fun ChoiceStarsDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceStars && state.starsList.isNotEmpty()){
        SingleChoiceBottomSheet(
            title = "Минимальная звездность отеля",
            items = state.starsList.map { it.name },
            selectedItem = state.star?.name,
            onDismiss = { viewModel.showChoiceStars(false) }
        ){
            val star = it?.let { state.starsList[it] }
            viewModel.changeStars(star)
            viewModel.showChoiceStars(false)
        }
    }
}

@Composable
private fun TitleWithTextField(
    title: String,
    placeholder: String,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    onTextChange: (String) -> Unit
) {
    TitleWithContent(title) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            textStyle = LocalTextStyle.current.copy(color = Colors.GRAY, fontSize = 18.sp),
            placeholder = {
                Text(text = placeholder, color = Colors.GRAY_LIGHT, fontSize = 18.sp)
            },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,

            colors = TextFieldDefaults.colors(
                disabledContainerColor = Colors.WHITE,
                focusedContainerColor = Colors.WHITE,
                errorContainerColor = Colors.WHITE,
                unfocusedContainerColor = Colors.WHITE,
                focusedIndicatorColor = Colors.ORANGE,
                unfocusedIndicatorColor = Colors.ORANGE,
                disabledIndicatorColor = Colors.ORANGE,
            ),
        )

    }
}

@Composable
private fun TitleWithButton(
    title: String,
    textButton: String?,
    onCancel: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    TitleWithContent(title) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Colors.WHITE),
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Colors.ORANGE)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = textButton ?: "Не выбрано",
                    fontSize = 16.sp,
                    color = if (textButton.isNullOrEmpty()) Colors.GRAY_LIGHT else Colors.ORANGE,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                onCancel?.let {
                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd),
                        onClick = it
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            tint = Colors.GRAY
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun TitleWithContent(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = Colors.GRAY,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}