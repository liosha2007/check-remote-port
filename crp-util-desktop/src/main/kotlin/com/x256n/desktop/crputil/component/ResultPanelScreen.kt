package com.x256n.desktop.crputil.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.x256n.desktop.crputil.ActivePanel
import com.x256n.desktop.crputil.DisplayResultItem
import com.x256n.desktop.crputil.LogMessage
import com.x256n.desktop.crputil.MainViewModel

@Composable
fun ResultPanelScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val state = viewModel.state.value
    val multiplier = if (state.activePanel == ActivePanel.RESULT) 1.6f else 1f

    Column(
        modifier = modifier.background(Color.Black).fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            if (state.result.isNotEmpty()) {
                MessageItemScreen(multiplier, LogMessage.InfoMessage("Результати"))
                ResultHeaserScreen(multiplier)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(state.result) { item ->
                        when (item) {
                            is DisplayResultItem.Message -> {
                                MessageItemScreen(multiplier, item.message)
                            }
                            is DisplayResultItem.Delimiter -> {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth().height(1.dp)
                                        .padding(horizontal = 15.dp)
                                        .background(Color.Green)
                                )
                            }
                            is DisplayResultItem.AnswerUp -> {
                                val time = if (item.time % 1000 == 0) {
                                    "${item.time / 1000}sec"
                                } else {
                                    "${item.time}ms"
                                }
                                ItemScreen(
                                    multiplier = multiplier,
                                    ip = item.ip,
                                    port = item.port,
                                    time = time,
                                    host = item.host,
                                    viewModel = viewModel
                                ) {
                                    LogMessage.SuccessMessage(it)
                                }
                            }
                            is DisplayResultItem.AnswerDown -> {
                                val timeout = if (item.timeout % 1000 == 0) {
                                    "${item.timeout / 1000}sec"
                                } else {
                                    "${item.timeout}ms"
                                }
                                ItemScreen(
                                    multiplier = multiplier,
                                    ip = item.ip,
                                    port = item.port,
                                    time = "n/a (>$timeout)",
                                    host = item.host,
                                    viewModel = viewModel
                                ) {
                                    LogMessage.WarningMessage(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultHeaserScreen(multiplier: Float) {
    Row {
        Text(
            modifier = Modifier.width(95.dp * multiplier),
            text = "IP",
            style = TextStyle(
                fontSize = 10.sp * multiplier,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W600
            )
        )
        Text(
            modifier = Modifier.width(45.dp * multiplier),
            text = "port",
            style = TextStyle(
                fontSize = 10.sp * multiplier,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W600
            )
        )
        Text(
            modifier = Modifier.width(85.dp * multiplier),
            text = "time",
            style = TextStyle(
                fontSize = 10.sp * multiplier,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W600
            )
        )
    }
}

@Composable
fun ItemScreen(
    multiplier: Float,
    ip: String,
    port: Int,
    time: String,
    host: String,
    viewModel: MainViewModel,
    LogMessageBuilder: (value: String) -> LogMessage
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.width(95.dp * multiplier)
                .clickable {
                    viewModel.onCopyToClipboard(ip)
                },
            text = buildAnnotatedString {
                AnnotatedLogMessageScreen(LogMessageBuilder(ip))
            }, style = MaterialTheme.typography.body1.copy(
                fontSize = 10.sp * multiplier,
            )
        )
        Text(
            modifier = Modifier.width(45.dp * multiplier)
                .clickable {
                    viewModel.onCopyToClipboard("$ip:$port")
                },
            text = buildAnnotatedString {
                AnnotatedLogMessageScreen(LogMessageBuilder(":$port"))
            }, style = MaterialTheme.typography.body1.copy(
                fontSize = 10.sp * multiplier,
            )
        )
        Text(
            modifier = Modifier.width(85.dp * multiplier), text = buildAnnotatedString {
                AnnotatedLogMessageScreen(LogMessageBuilder(time))
            }, style = MaterialTheme.typography.body1.copy(
                fontSize = 10.sp * multiplier,
            )
        )
        if (host.isEmpty()) {
            Text(
                modifier = Modifier.weight(1f)
                    .clickable {
                        viewModel.onResolveHost(ip)
                    },
                text = "?",
                style = TextStyle(
                    fontSize = 10.sp * multiplier,
                    color = Color.Blue,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.W600
                )
            )
        } else {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.onCopyToClipboard(host)
                    },
                text = buildAnnotatedString {
                    AnnotatedLogMessageScreen(LogMessageBuilder(host))
                }, style = MaterialTheme.typography.body1.copy(
                    fontSize = 10.sp * multiplier,
                )
            )
        }
    }
}

@Composable
fun MessageItemScreen(multiplier: Float, log: LogMessage) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(4.dp), text = buildAnnotatedString {
            AnnotatedLogMessageScreen(log)
        }, style = MaterialTheme.typography.body1.copy(
            fontSize = 10.sp * multiplier,
        )
    )
}