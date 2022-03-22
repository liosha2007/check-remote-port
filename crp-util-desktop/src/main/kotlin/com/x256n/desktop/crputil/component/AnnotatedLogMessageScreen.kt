package com.x256n.desktop.crputil.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.x256n.desktop.crputil.LogMessage

@Composable
fun AnnotatedString.Builder.AnnotatedLogMessageScreen(log: LogMessage) {
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