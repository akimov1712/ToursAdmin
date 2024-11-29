package ru.topbun.toursadmin.presentation.screens.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.PostingScreenState.Success
import ru.topbun.toursadmin.presentation.screens.settings.SettingsState.SettingsScreenState.Loading
import ru.topbun.toursadmin.presentation.screens.settings.components.DialogDatePicker
import ru.topbun.toursadmin.presentation.screens.settings.components.MultipleChoiceBottomSheet
import ru.topbun.toursadmin.presentation.screens.settings.components.SingleChoiceBottomSheet
import ru.topbun.toursadmin.presentation.screens.settings.components.StockItem
import ru.topbun.toursadmin.presentation.theme.Colors
import ru.topbun.toursadmin.utills.formatDate

object SettingsScreen: Screen{

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val context = LocalContext.current
            val viewModel = rememberScreenModel { SettingsViewModel() }
            val state by viewModel.state.collectAsState()
            val screenState = state.screenState
            val postingState = state.postingState
            when(screenState){
                is SettingsState.SettingsScreenState.Error -> {
                    LaunchedEffect(screenState) {
                        Toast.makeText(context, screenState.msg, Toast.LENGTH_SHORT).show()
                        viewModel.resetScreenState()
                    }
                }
                Loading -> CircularProgressIndicator(color = Colors.ORANGE)
                else -> {}
            }
            if (screenState != Loading){
                ContentSuccess(viewModel, state)
            }
            if (postingState == Success){
                LaunchedEffect(postingState) {
                    Toast.makeText(context, "Конфиг обновлен", Toast.LENGTH_SHORT).show()
                    viewModel.resetPostingState()
                }
            }

        }
    }
}

@Composable
fun ContentSuccess(viewModel: SettingsViewModel, state: SettingsState) {
    val state by viewModel.state.collectAsState()
    println("State: ${state.selectableConfigId}")
    if (state.selectableConfigId == null){
        ChoiceConfig(state, viewModel)
    } else {
        BackHandler{ viewModel.changeSelectableConfigId(null) }
        ManageConfig(viewModel)
    }


}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun ChoiceConfig(
    state: SettingsState,
    viewModel: SettingsViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(
                items = state.configs,
            ) { index, config ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.WHITE)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple()
                        ) { viewModel.changeSelectableConfigId(index) }
                        .border(1.dp, Colors.ORANGE, RoundedCornerShape(8.dp))
                        .padding(16.dp, 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = config.title.takeIf { it.isNotEmpty() } ?: "Без названия",
                        fontSize = 20.sp,
                        color = Colors.ORANGE,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }
        Button(
            modifier = Modifier
                .height(36.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(Colors.ORANGE),
            shape = RoundedCornerShape(8.dp),
            onClick = viewModel::addConfig
        ) {
            Text(text = "Добавить фильтр", color = Colors.GRAY, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ManageConfig(
    viewModel: SettingsViewModel,
) {
    val state by viewModel.state.collectAsState()
    state.stateConfig()?.let { config ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp, 10.dp, 20.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { viewModel.changeSelectableConfigId(null) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Colors.ORANGE
                    )
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (config.title.isEmpty()){
                        Text(
                            text = "Названия",
                            color = Colors.GRAY_LIGHT,
                            fontSize = 20.sp
                        )
                    }
                    BasicTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = config.title,
                        onValueChange = viewModel::changeTitle,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Colors.ORANGE,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600
                        )
                    )
                }
                IconButton(onClick = { viewModel.deleteConfig() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Colors.ORANGE
                    )
                }
            }
            TitleWithButton("Город вылета", config.city?.name) { viewModel.showChoiceCity(true) }
            TitleWithTextField(
                title = "Ограничить дату вылета, на ближайшие дни. Если 0 – период не ограничен",
                placeholder = "Количество дней",
                text = config.maxDays?.toString() ?: ""
            ) { if (it.length < 4) viewModel.changeMaxDays(it) }
            val countriesTextButton =
                config.countries.joinToString { it.name }.takeIf { config.countries.isNotEmpty() }
            TitleWithButton("Страны", countriesTextButton) { viewModel.showChoiceCountry(true) }

            val regionTextButton =
                config.regions.joinToString { it.name }.takeIf { config.regions.isNotEmpty() }
            TitleWithButton("Регионы", regionTextButton) { viewModel.showChoiceRegion(true) }

            val operatorTextButton =
                config.operators.joinToString { it.name }.takeIf { config.operators.isNotEmpty() }
            TitleWithButton("Туроператоры", operatorTextButton) { viewModel.showChoiceOperator(true) }

            TitleWithButton(
                title = "С даты",
                textButton = config.fromDate?.formatDate(),
                onCancel = config.fromDate?.let { { viewModel.changeFromDate(null) } }
            ) { viewModel.showChoiceFromDate(true) }

            TitleWithButton(
                title = "По дату",
                textButton = config.toDate?.formatDate(),
                onCancel = config.toDate?.let { { viewModel.changeToDate(null) } }
            ) { viewModel.showChoiceToDate(true) }

            TitleWithButton(
                "Мин. звездность отелей",
                config.star?.name
            ) { viewModel.showChoiceStars(true) }
            TitleWithTextField(
                title = "Мин. рейтинг отелей",
                placeholder = "Рейтинг отелей",
                text = config.minRating ?: "",
                keyboardType = KeyboardType.Decimal
            ) { if (it.length < 4) viewModel.changeRating(it) }
            TitleWithButton("Тип питания", config.meal?.russian) { viewModel.showChoiceMeal(true) }
            TitleWithTextField(
                title = "Задержка между уникальными постами в днях",
                placeholder = "Количество дней",
                text = config.delayUniquePosts?.toString() ?: ""
            ) { if (it.length < 4) viewModel.changeDelayUniquePosts(it) }
            TitleWithTextField(
                title = "Задержка между постами в минутах",
                placeholder = "Количество минут",
                text = config.delayPostingMinutes?.toString() ?: ""
            ) { if (it.length < 4) viewModel.changeDelayPostingMinutes(it) }
            TitleWithTextField(
                title = "Домен сайта",
                placeholder = "Введите домен",
                text = config.domain,
                keyboardType = KeyboardType.Text
            ) { viewModel.changeDomain(it) }
            TitleWithStocks(viewModel, state)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Colors.ORANGE),
                shape = RoundedCornerShape(8.dp),
                onClick = viewModel::setConfig
            ) {
                Text(text = "Отправить", color = Colors.GRAY, fontSize = 20.sp)
            }
            SetPickDialogs(viewModel = viewModel)
        }
    } ?: run { viewModel.changeSelectableConfigId(null) }

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
    ChoiceStackToOperatorDialog(viewModel, state)
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
private fun ChoiceStackToOperatorDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceToStockOperator != null && state.operatorList.isNotEmpty()){
        val config = state.stateConfig()
        SingleChoiceBottomSheet(
            title = "Туроператоры",
            items = state.operatorList.map { it.name },
            selectedItem = config?.stocks?.getOrNull(state.showChoiceToStockOperator)?.operator?.name,
            onDismiss = { viewModel.showChoiceToStackOperator(null) }
        ){
            val operator = it?.let { state.operatorList[it] }
            viewModel.changeStockOperator(operator, state.showChoiceToStockOperator)
            viewModel.showChoiceToStackOperator(null)
        }
    }
}

@Composable
private fun ChoiceOperatorDialog(viewModel: SettingsViewModel, state: SettingsState) {
    if (state.showChoiceOperators && state.operatorList.isNotEmpty()){
        val config = state.stateConfig()
        MultipleChoiceBottomSheet(
            title = "Туроператоры",
            items = state.operatorList.map { it.name },
            selectedItems = config?.operators?.map { it.name } ?: emptyList(),
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
        val config = state.stateConfig()
        MultipleChoiceBottomSheet(
            title = "Регионы",
            items = state.regionList.map { it.name },
            selectedItems = config?.regions?.map { it.name } ?: emptyList(),
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
        val config = state.stateConfig()
        MultipleChoiceBottomSheet(
            title = "Страны",
            items = state.countryList.map { it.name },
            selectedItems = config?.countries?.map { it.name } ?: emptyList(),
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
        val config = state.stateConfig()
        SingleChoiceBottomSheet(
            title = "Город вылета",
            items = state.cityList.map { it.name },
            selectedItem = config?.city?.name,
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
        val config = state.stateConfig()
        SingleChoiceBottomSheet(
            title = "Тип питания",
            items = state.mealList.map { it.russian + " (${it.name})" },
            selectedItem = config?.meal?.name,
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
        val config = state.stateConfig()
        SingleChoiceBottomSheet(
            title = "Минимальная звездность отеля",
            items = state.starsList.map { it.name },
            selectedItem = config?.star?.name,
            onDismiss = { viewModel.showChoiceStars(false) }
        ){
            val star = it?.let { state.starsList[it] }
            viewModel.changeStars(star)
            viewModel.showChoiceStars(false)
        }
    }
}

@Composable
fun TitleWithStocks(viewModel: SettingsViewModel, state: SettingsState) {
    TitleWithContent(title = "Акции") {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val config = state.stateConfig()
            config?.stocks?.forEachIndexed { index, stock ->
                StockItem(
                    operator = stock.operator?.name,
                    stock = stock.stock,
                    onChangeOperator = { viewModel.showChoiceToStackOperator(index) },
                    onChangeStock = { viewModel.changeStockText(it, index) },
                    onClickRemove = { viewModel.removeStock(index) },
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(Colors.ORANGE),
                shape = RoundedCornerShape(8.dp),
                onClick = viewModel::addStock
            ){
                Text(text = "Добавить акцию", color = Colors.GRAY, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TitleWithTextField(
    title: String,
    placeholder: String,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    onTextChange: (String) -> Unit
) {
    TitleWithContent(title) {
        AppOutlinedTextField(
            placeholder = placeholder,
            text = text,
            keyboardType = keyboardType,
            onTextChange = onTextChange,
        )
    }
}

@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
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

@Composable
fun AppOutlinedButton(
    textButton: String?,
    onCancel: (() -> Unit)? = null,
    modifier:Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
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

@Composable
fun TitleWithButton(
    title: String,
    textButton: String?,
    onCancel: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    TitleWithContent(title) {
        AppOutlinedButton(
            textButton = textButton,
            onCancel = onCancel,
            onClick = onClick,
        )
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