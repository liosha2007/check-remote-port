package com.x256n.desktop.crputil
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.x256n.desktop.crputil.component.BottomPanelScreen
import com.x256n.desktop.crputil.component.EndPanelScreen
import com.x256n.desktop.crputil.component.StartPanelScreen

@Composable
@Preview
fun App() {
    val source = remember { mutableStateOf("") }
    var result by remember { mutableStateOf<MutableList<LogMessage>>(mutableListOf()) }
    val timeout = remember { mutableStateOf(3000) }
    var progress by remember { mutableStateOf(0f) }
    var readOnly by remember { mutableStateOf(false) }
    val showTcpFormat = remember { mutableStateOf(false) }
    var activeStart by remember { mutableStateOf(true) }
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
                StartPanelScreen(
                    modifier = if (activeStart) Modifier.weight(1f) else Modifier.width(275.dp),
                    source, activeStart, readOnly, progress
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(15.dp).clickable {
                            activeStart = !activeStart
                        }
                )
                EndPanelScreen(
                    result,
                    !activeStart,
                    modifier = (if (activeStart) Modifier.width(275.dp) else Modifier.weight(1f))
                )
            }
            BottomPanelScreen(
                readOnly,
                timeout,
                showTcpFormat,
                scope,
                analizer,
                source,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
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
