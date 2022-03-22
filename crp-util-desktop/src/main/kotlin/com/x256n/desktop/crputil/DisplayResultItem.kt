package com.x256n.desktop.crputil

sealed class DisplayResultItem {
    data class AnswerUp(val ip: String, val port: Int, val time: Int, val host: String = "") : DisplayResultItem()
    data class AnswerDown(val ip: String, val port: Int, val timeout: Int, val host: String = "") : DisplayResultItem()
    data class Message(val message: LogMessage) : DisplayResultItem()
    object Delimiter : DisplayResultItem()
}
