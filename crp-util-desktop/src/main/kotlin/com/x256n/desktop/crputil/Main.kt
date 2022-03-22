package com.x256n.desktop.crputil
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    var source by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<MutableList<LogMessage>>(mutableListOf()) }
    var timeout by remember { mutableStateOf(3000) }
    var progress by remember { mutableStateOf(0f) }
    var readOnly by remember { mutableStateOf(false) }
    var showTcpFormat by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val logger = remember {
        ScreenLogger(
            onLogError = {
                result = result.toMutableList().apply { add(LogMessage.ErrorMessage(it)) }
            },
            onLogWarn = {
                result = result.toMutableList().apply { add(LogMessage.WarningMessage(it)) }
            },
            onLogInfo = {
                result = result.toMutableList().apply { add(LogMessage.InfoMessage(it)) }
            },
            onLogSuccess = {
                result = result.toMutableList().apply { add(LogMessage.SuccessMessage(it)) }
            },
            onClear = {
                result = mutableListOf()
            },
        )
    }
    val analizer = remember {
        InputAnalizer(
            logger = logger,
            onStart = { readOnly = true },
            onProgress = { progress = it },
            onFinish = { readOnly = false }
        )
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(5.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(modifier = Modifier.weight(1f)) {
                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f),
                        value = source,
                        readOnly = readOnly,
                        onValueChange = { source = it },
                        label = { Text("Вхідні дані (у яких є IP адреса та порти до неї)") },
                    )
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                            .height(5.dp),
                        progress = progress
                    )
                }
                Column(
                    modifier = Modifier.width(250.dp).padding(start = 10.dp),
                ) {
                    SelectionContainer(
                        modifier = Modifier.fillMaxHeight()
                            .border(BorderStroke(1.dp, Color.Gray))
                    ) {
                        Text(
                            modifier = Modifier.verticalScroll(rememberScrollState(0))
                                .fillMaxSize()
                                .padding(4.dp),
                            text = buildAnnotatedString {
                                result.forEach { log ->
                                    when (log) {
                                        is LogMessage.InfoMessage -> {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color.Black,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            ) {
                                                append(log.message)
                                            }
                                        }
                                        is LogMessage.WarningMessage -> {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color(0xFFFFA700),
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            ) {
                                                append(log.message)
                                            }
                                        }
                                        is LogMessage.ErrorMessage -> {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color.Red,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            ) {
                                                append(log.message)
                                            }
                                        }
                                        is LogMessage.SuccessMessage -> {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color.Green,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            ) {
                                                append(log.message)
                                            }
                                        }
                                    }
                                }
                            },
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
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
                            enabled = !readOnly,
                            value = timeout.toString(),
                            readOnly = readOnly,
                            maxLines = 1,
                            onValueChange = { timeout = if (it.isEmpty()) 0 else it.toInt() },
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
                            checked = showTcpFormat, onCheckedChange = {
                                showTcpFormat = !showTcpFormat
                            })
                        Button(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    analizer.analize(source, timeout, showTcpFormat)
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
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "crp-util (Check remote port)",
    icon = painterResource("icon.png")
    ) {
        App()
    }
}
