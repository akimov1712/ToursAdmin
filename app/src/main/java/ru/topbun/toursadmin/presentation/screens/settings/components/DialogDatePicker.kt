package ru.topbun.toursadmin.presentation.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.util.date.GMTDate
import ru.topbun.toursadmin.presentation.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDatePicker(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onConfirm: (GMTDate) -> Unit,
    onCancel: (GMTDate) -> Unit,
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(Colors.WHITE),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let { onConfirm(GMTDate(it)) }},
            ){
                Text(text = "ОК", color = Colors.ORANGE)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let { onCancel(GMTDate(it)) }},
            ){
                Text(text = "Отмена", color = Colors.ORANGE)
            }
        },
    ) {
        DatePicker(
            modifier = Modifier.background(Colors.WHITE, RoundedCornerShape(20.dp)),
            state = state,
            showModeToggle = false,
            title = null,
            headline = {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                    fontSize = 24.sp,
                    color = Colors.GRAY,
                    text = state.selectedDateMillis?.let { GMTDate(it) }?.let { "${it.month.name.take(3)} ${it.dayOfMonth}, ${it.year}" } ?: "Выберите дату"
                )
            },
            colors = DatePickerDefaults.colors(
                containerColor = Colors.WHITE,
                selectedDayContentColor = Colors.WHITE,
                selectedDayContainerColor = Colors.ORANGE,
                selectedYearContentColor = Colors.WHITE,
                selectedYearContainerColor = Colors.ORANGE,
                todayDateBorderColor = Colors.ORANGE,
            )
        )
    }
}