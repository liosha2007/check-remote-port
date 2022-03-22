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
                status = it,
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
                            val host = resolveHost(item.ip)
                            if (host == null) {
                                item.copy(host = "-")
                            } else {
                                item.copy(host = host)
                            }
                        } else item
                    }
                    is DisplayResultItem.AnswerDown -> {
                        if (item.ip == ip) {
                            val host = resolveHost(item.ip)
                            if (host == null) {
                                item.copy(host = "-")
                            } else {
                                item.copy(host = host)
                            }
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

    private fun resolveHost(ip: String): String? =
        try {
            val addr = InetAddress.getByName(ip)
            if (ip == addr.hostName) {
                throw Exception()
            } else {
                addr.hostName
            }
        } catch (e: Exception) {
            null
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
                status = "Немає вхідних данних!"
            )
            return
        }
        val pinger = PortPinger(
            logger = logger,
            onStart = {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        mode = ScreenMode.PROGRESS
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
                        mode = ScreenMode.MAIN
                    )
                }
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            pinger.analize(state.value.source, state.value.timeout)?.let { result ->
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        result = result,
                        activePanel = ActivePanel.RESULT,
                        status = "Натисніть на ip або порт щоб скопіювати їх. Натисніть на зображення меню (біля IP) щоб скопіювати всі дані"
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
        copyToClipboard(value)
    }

    fun onCopyGreen() {
        val content = state.value.result
            .filterIsInstance<DisplayResultItem.AnswerUp>()
            .map { it.ip }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyGreenWithPorts() {
        val content = state.value.result
            .filterIsInstance<DisplayResultItem.AnswerUp>()
            .map { "${it.ip}:${it.port}" }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyGreenCustom() {
        _state.value = _state.value.copy(
            mode = ScreenMode.CUSTOM_GREEN
        )
        fomatCustom()
    }

    fun onCopyYellow() {
        val content = state.value.result
            .filterIsInstance<DisplayResultItem.AnswerDown>()
            .map { it.ip }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyYellowWithPorts() {
        val content = state.value.result
            .filterIsInstance<DisplayResultItem.AnswerDown>()
            .map { "${it.ip}:${it.port}" }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyYellowCustom() {
        _state.value = _state.value.copy(
            mode = ScreenMode.CUSTOM_YELLOW
        )
        fomatCustom()
    }

    fun onCopyAll() {
        val content = state.value.result
            .filter { it is DisplayResultItem.AnswerUp || it is DisplayResultItem.AnswerDown }
            .map {
                if (it is DisplayResultItem.AnswerUp) it.ip
                else if (it is DisplayResultItem.AnswerDown) it.ip
                else ""
            }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyAllWithPorts() {
        val content = state.value.result
            .map {
                if (it is DisplayResultItem.AnswerUp) "${it.ip}:${it.port}"
                else if (it is DisplayResultItem.AnswerDown) "${it.ip}:${it.port}"
                else ""
            }
            .joinToString(" ")
        copyToClipboard(content)
    }

    fun onCopyAllCustom() {
        _state.value = _state.value.copy(
            mode = ScreenMode.CUSTOM_ALL
        )
        fomatCustom()
    }

    private fun copyToClipboard(value: String) {
        Toolkit.getDefaultToolkit().systemClipboard
            .setContents(StringSelection(value), null)
    }

    fun onTemplateChange(template: String) {
        _state.value = _state.value.copy(
            customTemplate = template
        )
        fomatCustom()
    }

    fun fomatCustom() {
        val template = state.value.customTemplate
        val customValue = when (state.value.mode) {
            ScreenMode.CUSTOM_GREEN -> {
                state.value.result.filterIsInstance<DisplayResultItem.AnswerUp>()
                    .map { formatWithTemplate(it.ip, it.port, template) }.joinToString("")
            }
            ScreenMode.CUSTOM_YELLOW -> {
                state.value.result.filterIsInstance<DisplayResultItem.AnswerDown>()
                    .map { formatWithTemplate(it.ip, it.port, template) }.joinToString("")
            }
            ScreenMode.CUSTOM_ALL -> {
                state.value.result.filter { it is DisplayResultItem.AnswerUp || it is DisplayResultItem.AnswerDown }
                    .joinToString("") {
                        when (it) {
                            is DisplayResultItem.AnswerUp -> {
                                formatWithTemplate(it.ip, it.port, template)
                            }
                            is DisplayResultItem.AnswerDown -> {
                                formatWithTemplate(it.ip, it.port, template)
                            }
                            else -> {
                                ""
                            }
                        }
                    }
            }
            else -> {
                _state.value = _state.value.copy(
                    status = "Невідомий режим"
                )
                ""
            }
        }
        _state.value = _state.value.copy(
            customTemplate = template,
            customValue = customValue
        )
    }

    private fun formatWithTemplate(ip: String, port: Int, template: String) =
        template
            .replace("#IP", ip)
            .replace("#PORT", port.toString())

    fun onCancelCustom() {
        _state.value = _state.value.copy(
            mode = ScreenMode.MAIN
        )
    }
}