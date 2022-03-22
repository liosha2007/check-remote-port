package com.x256n.desktop.crputil.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.x256n.desktop.crputil.LogMessage

@Composable
fun EndPanelScreen(result: MutableList<LogMessage>, isActive: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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
                    result.forEach { log ->
                        when (log) {
                            is LogMessage.InfoMessage -> {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.W700
                                    )
                                ) {
                                    append(log.message)
                                }
                            }
                            is LogMessage.WarningMessage -> {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFFFFA700),
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.W700
                                    )
                                ) {
                                    append(log.message)
                                }
                            }
                            is LogMessage.ErrorMessage -> {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.W700
                                    )
                                ) {
                                    append(log.message)
                                }
                            }
                            is LogMessage.SuccessMessage -> {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Green,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.W700
                                    )
                                ) {
                                    append(log.message)
                                }
                            }
                        }
                    }
                },
                style = MaterialTheme.typography.body1.copy(
                    fontSize = if (isActive) 16.sp else 12.sp
                )
            )
        }
    }
}