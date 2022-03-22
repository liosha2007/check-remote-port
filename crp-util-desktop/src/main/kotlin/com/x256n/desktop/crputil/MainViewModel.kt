package com.x256n.desktop.crputil

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.x256n.desktop.crputil.common.isDigit
import kotlinx.coroutines.*
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.InetAddress


class MainViewModel {
    private val _state: MutableState<MainState> = mutableStateOf(MainState())
    val state: State<MainState> = _state

    val logger = ScreenLogger(
        onLogError = {
            _state.value = _state.value.copy(
                logs = _state.value.logs.apply { add(LogMessage.ErrorMessage(it)) }
            )
        },
        onLogWarn = {
            _state.value = _state.value.copy(
                logs = _state.value.logs.apply { add(LogMessage.WarningMessage(it)) }
            )
        },
        onLogInfo = {
            _state.value = _state.value.copy(
                logs = _state.value.logs.apply { add(LogMessage.InfoMessage(it)) }
            )
        },
        onLogSuccess = {
            _state.value = _state.value.copy(
                logs = _state.value.logs.apply { add(LogMessage.SuccessMessage(it)) }
            )
        },
        onClear = {
            _state.value = _state.value.copy(
                logs = mutableListOf()
            )
        },
    )

    fun onResolveHost(ip: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = state.value.result.map { item ->
                when (item) {
                    is DisplayResultItem.AnswerUp -> {
                        if (item.ip == ip) {
                            item.copy(
                                host = resolveHost(item.ip)
                            )
                        } else item
                    }
                    is DisplayResultItem.AnswerDown -> {
                        if (item.ip == ip) {
                            item.copy(
                                host = resolveHost(item.ip)
                            )
                        } else item
                    }
                    else -> {
                        item
                    }
                }
            }
            withContext(Dispatchers.IO) {
                _state.value = _state.value.copy(
                    result = result
                )
            }
        }
    }

    private fun resolveHost(ip: String): String =
        try {
            val addr = InetAddress.getByName(ip)
            if (ip == addr.hostName) {
                throw Exception()
            } else {
                addr.hostName
            }
        } catch (e: Exception) {
            "Unknown"
        }


    fun onSourceChanged(value: String) {
        _state.value = _state.value.copy(
            source = value
        )
    }

    fun onAnalizeClick() {
        _state.value = MainState(
            source = state.value.source,
            timeout = state.value.timeout,
        )
        if (state.value.source.isBlank()) {
            _state.value = _state.value.copy(
                logs = mutableListOf(LogMessage.WarningMessage("Немає вхідних данних!"))
            )
        }
        val pinger = PortPinger(
            logger = logger,
            onStart = {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        isInProgress = true
                    )
                }
            },
            onProgress = {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        progress = it
                    )
                }
            },
            onFinish = {
                delay(500)
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        isInProgress = false
                    )
                }
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            pinger.analize(state.value.source, state.value.timeout)?.let { result ->
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        result = result,
                        activePanel = ActivePanel.RESULT
                    )
                }
            }
        }
    }

    fun onTimeoutChange(value: String) {
        if (value.isDigit()) {
            _state.value = _state.value.copy(
                timeout = value.toInt()
            )
        }
    }

    fun onActivePanelChange() {
        _state.value = _state.value.copy(
            activePanel = if (_state.value.activePanel == ActivePanel.SOURCE) ActivePanel.RESULT else ActivePanel.SOURCE
        )
    }

    fun onCopyToClipboard(value: String) {
        Toolkit.getDefaultToolkit().systemClipboard
            .setContents(StringSelection(value), null)
    }
}