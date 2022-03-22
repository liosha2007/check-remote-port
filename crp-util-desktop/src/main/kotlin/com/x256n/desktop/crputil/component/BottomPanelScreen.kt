package com.x256n.desktop.crputil.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.x256n.desktop.crputil.InputAnalizer
import com.x256n.desktop.crputil.common.isDigit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BottomPanelScreen(
    readOnly: Boolean,
    timeout: MutableState<Int>,
    showTcpFormat: MutableState<Boolean>,
    scope: CoroutineScope,
    analizer: InputAnalizer,
    source: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Таймаут (мс)"
                )
                Spacer(modifier = Modifier.size(4.dp))
                TextField(modifier = Modifier
                    .height(50.dp),
                    enabled = !readOnly,
                    value = timeout.value.toString(),
                    readOnly = readOnly,
                    maxLines = 1,
                    onValueChange = { timeout.value = if (it.isEmpty()) 0 else if (it.isDigit()) it.toInt() else timeout.value },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    enabled = !readOnly,
                    checked = showTcpFormat.value, onCheckedChange = {
                        showTcpFormat.value = !showTcpFormat.value
                    })
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            analizer.analize(source.value, timeout.value, showTcpFormat.value)
                        }
                    },
                    enabled = !readOnly
                ) {
                    Text("Запустити перевірку!")
                }
            }
        }
    }
}