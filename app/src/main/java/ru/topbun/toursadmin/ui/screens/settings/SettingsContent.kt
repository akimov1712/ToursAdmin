package ru.topbun.toursadmin.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import ru.topbun.toursadmin.ui.theme.Colors

object SettingsScreen: Screen{
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                TitleWithButton("Страны", null){}
                TitleWithButton("Регионы", null){}
            }
            Row{
                TitleWithButton("Туроператоры", null){}
                TitleWithButton("Минимальная звездность отелей", null){}
            }
            Row{
                TitleWithButton("Минимальный рейтинг отелей", null){}
            }
        }
    }

}

@Composable
fun RowScope.TitleWithButton(title: String, textButton: String?, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().weight(1f)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = Colors.GRAY,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Colors.ORANGE)
        ) {
            Text(
                text = textButton ?: "Не выбрано",
                fontSize = 16.sp,
                color = Colors.ORANGE,
                fontWeight = FontWeight.W500
            )
        }
    }
}