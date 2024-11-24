package ru.topbun.toursadmin.presentation.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import ru.topbun.toursadmin.presentation.theme.Colors

@Composable
fun MultipleChoiceBottomSheet(
    title: String,
    items: List<String>,
    selectedItems: List<String>,
    onDismiss: () -> Unit,
    onSelectedItem: (List<Int>) -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = onDismiss,
        properties = BottomSheetDialogProperties(
            behaviorProperties = BottomSheetBehaviorProperties(
                state = BottomSheetBehaviorProperties.State.Expanded
            )
        )
    ) {
        val selectedItemsForIndex = selectedItems.map { items.indexOf(it) }
        var selectedIndexes by remember { mutableStateOf(selectedItemsForIndex.toSet()) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.WHITE)
                .padding(vertical = 24.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row {
                Text(
                    text = title,
                    color = Colors.GRAY,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = selectedIndexes.size.toString(),
                    color = Colors.GRAY,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.weight(1f))
                if (selectedIndexes.isNotEmpty()){
                    Text(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedIndexes = emptySet()
                        },
                        text = "Очистить все",
                        color = Colors.ORANGE,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W700
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(items){ index, item ->
                    MultipleChoiceItem(
                        text = item,
                        checked = selectedIndexes.contains(index),
                    ){
                        selectedIndexes = selectedIndexes.toMutableSet().apply {
                            if (it) add(index) else remove(index)
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Colors.ORANGE),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onSelectedItem(selectedIndexes.toList())
                }
            ){
                Text(text = "Сохранить", color = Colors.GRAY, fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun MultipleChoiceItem(
    text: String ,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            ) { onChecked(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                uncheckedColor = Colors.ORANGE,
                checkedColor = Colors.ORANGE,
                checkmarkColor = Colors.WHITE,

            )
        )
        Text(
            text = text,
            color = Colors.GRAY,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600
        )
    }
}