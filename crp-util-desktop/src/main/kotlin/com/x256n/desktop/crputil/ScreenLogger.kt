package com.x256n.desktop.crputil

class ScreenLogger(
    private val onLogError: (mesage: String) -> Unit,
    private val onLogWarn: (mesage: String) -> Unit,
    private val onLogInfo: (mesage: String) -> Unit,
    private val onLogSuccess: (mesage: String) -> Unit,

    private val onClear: () -> Unit
) {
    fun error(mesage: String) = onLogError(mesage)
    fun warning(mesage: String) = onLogWarn(mesage)
    fun info(mesage: String) = onLogInfo(mesage)
    fun success(mesage: String) = onLogSuccess(mesage)

    fun clear() = onClear()
    fun newLine() = onLogInfo("\r\n")
}

sealed class LogMessage(val message: String) {
    class SuccessMessage(message: String) : LogMessage(message)
    class InfoMessage(message: String) : LogMessage(message)
    class ErrorMessage(message: String) : LogMessage(message)
    class WarningMessage(message: String) : LogMessage(message)
}