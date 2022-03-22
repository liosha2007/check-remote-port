package com.x256n.desktop.crputil

data class MainState(
    val mode: ScreenMode = ScreenMode.MAIN,
    val source: String = "",
    val result: List<DisplayResultItem> = emptyList(),
    val activePanel: ActivePanel = ActivePanel.SOURCE,
    val progress: Float = 0f,
    val logs: MutableList<LogMessage> = mutableListOf(),
    val timeout: Int = 3000,
    val status: String = "Введіть вхідні дані та почніть перевірку",
    val customTemplate: String = "tcp://#IP:#PORT ",
    val customValue: String = "",
)

enum class ActivePanel {
    SOURCE, RESULT
}

enum class ScreenMode {
    MAIN, PROGRESS, CUSTOM_YELLOW, CUSTOM_GREEN, CUSTOM_ALL
}
