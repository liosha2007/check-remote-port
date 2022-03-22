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
//    isInProgress: MutableState<Boolean>,
//    source: MutableState<String>,
//    progress: MutableState<Float>,
//    modifier: Modifier = Modifier,
//    console: MutableState<MutableList<LogMessage>>,

//    val timeout = remember { mutableStateOf(3000) }
//    val showTcpFormat = remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()

//    val logger = remember {
//        ScreenLogger(
//            onLogError = {
//                console.value = console.value.toMutableList().apply { add(LogMessage.ErrorMessage(it)) }
//            },
//            onLogWarn = {
//                console.value = console.value.toMutableList().apply { add(LogMessage.WarningMessage(it)) }
//            },
//            onLogInfo = {
//                console.value = console.value.toMutableList().apply { add(LogMessage.InfoMessage(it)) }
//            },
//            onLogSuccess = {
//                console.value = console.value.toMutableList().apply { add(LogMessage.SuccessMessage(it)) }
//            },
//            onClear = {
//                console.value = mutableListOf()
//            },
//        )
//    }
//    val analizer = remember {
//        InputAnalizer(
//            logger = logger,
//            onStart = { isInProgress.value = true },
//            onProgress = { progress.value = it },
//            onFinish = { isInProgress.value = false }
//        )
//    }

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
                        .height(50.dp),
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