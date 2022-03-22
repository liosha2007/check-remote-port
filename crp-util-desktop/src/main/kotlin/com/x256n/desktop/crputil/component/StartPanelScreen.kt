package com.x256n.desktop.crputil.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartPanelScreen(modifier: Modifier = Modifier, source: MutableState<String>, isActive: Boolean, readOnly: Boolean, progress: Float) {
    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            value = source.value,
            readOnly = readOnly,
            onValueChange = { source.value = it },
            label = { Text("Вхідні дані (у яких є IP адреса та порти до неї)") },
            textStyle = MaterialTheme.typography.caption.copy(
                fontSize = if (isActive) 16.sp else 10.sp
            )
        )
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
                .height(5.dp),
            progress = progress
        )
    }
}