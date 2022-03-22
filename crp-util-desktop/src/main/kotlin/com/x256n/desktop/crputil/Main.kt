package com.x256n.desktop.crputil
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "crp-util (Check remote port)",
        icon = painterResource("icon.png")
    ) {
        MaterialTheme {
            MainScreen(MainViewModel())
        }
    }
}
