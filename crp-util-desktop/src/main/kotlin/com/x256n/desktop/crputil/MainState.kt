package com.x256n.desktop.crputil

data class MainState(
    val isInProgress: Boolean = false,
    val source: String = "",
    val result: List<DisplayResultItem> = emptyList(),
    val activePanel: ActivePanel = ActivePanel.SOURCE,
    val progress: Float = 0f,
    val logs: MutableList<LogMessage> = mutableListOf(),
    val timeout: Int = 3000,
)

enum class ActivePanel {
    SOURCE, RESULT
}
