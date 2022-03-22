package com.x256n.desktop.crputil

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomFormatScreen(viewModel: MainViewModel) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            onValueChange = {},
            value = state.customValue
        )
        Row(
            modifier = Modifier
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = "Ваш формат: ",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W600
                )
            )
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                value = state.customTemplate,
                onValueChange = { viewModel.onTemplateChange(it) },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W600
                )
            )
            Button(onClick = {
                viewModel.onCancelCustom()
            }) {
                Text(text = "Повернутись")
            }
        }
    }
}