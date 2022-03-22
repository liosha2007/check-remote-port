package com.x256n.desktop.crputil.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.x256n.desktop.crputil.MainState

@Composable
fun BottomPanelScreen(modifier: Modifier = Modifier, state: MainState, onTimeoutChange: (String) -> Unit, onAnalizeClick: () -> Unit) {
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
                TextField(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(bottom = 4.dp),
                    value = state.timeout.toString(),
                    maxLines = 1,
                    onValueChange = onTimeoutChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onAnalizeClick
                ) {
                    Text("Запустити перевірку!")
                }
            }
        }
    }
}