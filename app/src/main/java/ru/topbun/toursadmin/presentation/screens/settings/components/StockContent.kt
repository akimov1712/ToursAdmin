package ru.topbun.toursadmin.presentation.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.topbun.toursadmin.presentation.screens.settings.AppOutlinedButton
import ru.topbun.toursadmin.presentation.screens.settings.AppOutlinedTextField
import ru.topbun.toursadmin.presentation.theme.Colors

@Composable
fun StockItem(
    operator: String?,
    stock: String,
    onChangeOperator: () -> Unit ,
    onChangeStock: (String) -> Unit ,
    onClickRemove: () -> Unit ,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        AppOutlinedButton(
            modifier = Modifier.weight(1f),
            textButton = operator,
            onClick = onChangeOperator
        )
        AppOutlinedTextField(
            modifier = Modifier.weight(1f),
            placeholder = "Текст акции",
            text = stock,
            keyboardType = KeyboardType.Text,
            onTextChange = onChangeStock
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Colors.WHITE)
                .clickable { onClickRemove() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(12.dp, 2.dp)
                    .background(Colors.ORANGE, CircleShape))
        }

    }
}
