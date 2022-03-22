package com.x256n.desktop.crputil

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.x256n.desktop.crputil.component.AnnotatedLogMessageScreen

@Composable
fun ConsoleScreen(state: MainState) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            SelectionContainer(
                modifier = Modifier.fillMaxHeight()
                    .border(BorderStroke(1.dp, Color.Gray))
                    .background(Color.Black)
            ) {
                Text(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState(0))
                        .fillMaxSize()
                        .padding(4.dp),
                    text = buildAnnotatedString {
                        state.logs.forEach { log ->
                            AnnotatedLogMessageScreen(log)
                        }
                    },
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 15.sp
                    )
                )
            }
        }
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
                .height(5.dp),
            progress = state.progress
        )
    }
}