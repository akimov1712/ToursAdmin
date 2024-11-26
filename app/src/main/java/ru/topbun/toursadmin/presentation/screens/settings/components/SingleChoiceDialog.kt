package ru.topbun.toursadmin.presentation.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import ru.topbun.toursadmin.presentation.theme.Colors

@Composable
fun SingleChoiceBottomSheet(
    title: String,
    items: List<String>,
    selectedItem: String?,
    onDismiss: () -> Unit,
    onSelectedItem: (index: Int?) -> Unit
) {
    val items = listOf("Не выбрано") + items
    BottomSheetDialog(
        onDismissRequest = onDismiss,
        properties = BottomSheetDialogProperties(
            behaviorProperties = BottomSheetBehaviorProperties(
                state = BottomSheetBehaviorProperties.State.Expanded
            )
        )
    ) {
        var selectedIndex by rememberSaveable { mutableStateOf(selectedItem?.let { items.indexOf(it) } ?: 0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .padding(vertical = 24.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = title,
                color = Colors.GRAY,
                fontSize = 28.sp,
                fontWeight = FontWeight.W600
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(items){ index, item ->
                    SingleChoiceItem(
                        text = item,
                        selected = selectedIndex == index,
                    ){ selectedIndex = index }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Colors.ORANGE),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val index = selectedIndex.takeIf { it != 0 }?.minus(1)
                    onSelectedItem(index)
                }
            ){
                Text(text = "Сохранить", color = Colors.GRAY, fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun SingleChoiceItem(
    text: String ,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember{MutableInteractionSource()}
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick() },
            enabled = true,
            colors = RadioButtonDefaults.colors(Colors.ORANGE,Colors.ORANGE,Colors.ORANGE,Colors.ORANGE)
        )
        Text(
            text = text,
            color = Colors.GRAY,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600
        )
    }
}