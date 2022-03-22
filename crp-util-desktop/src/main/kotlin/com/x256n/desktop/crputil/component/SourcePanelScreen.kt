package com.x256n.desktop.crputil.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.x256n.desktop.crputil.ActivePanel
import com.x256n.desktop.crputil.MainState

@Composable
fun SourcePanelScreen(modifier: Modifier = Modifier, state: MainState, onValueChange: (String) -> Unit) {
    val isActive = state.activePanel == ActivePanel.SOURCE
    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            value = state.source,
            readOnly = state.isInProgress,
            onValueChange = onValueChange,
            label = { Text("Вхідні дані (у яких є IP адреса та порти до неї)") },
            textStyle = MaterialTheme.typography.caption.copy(
                fontSize = if (isActive) 16.sp else 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = if (isActive) FontWeight.W500 else FontWeight.W700
            )
        )
    }
}