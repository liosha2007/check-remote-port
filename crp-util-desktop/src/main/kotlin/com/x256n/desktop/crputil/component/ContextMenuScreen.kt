package com.x256n.desktop.crputil.component

import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.x256n.desktop.crputil.DisplayResultItem
import com.x256n.desktop.crputil.MainViewModel

@Composable
fun ContextMenuScreen(menu: MutableState<Boolean>, viewModel: MainViewModel) {
    val state = viewModel.state
    val isGreenPresent = state.value.result.count { it is DisplayResultItem.AnswerUp } > 0
    val isYellowPresent = state.value.result.count { it is DisplayResultItem.AnswerDown } > 0

    DropdownMenu(
        expanded = menu.value,
        onDismissRequest = { menu.value = false }
    ) {
        val menuGreen = remember { mutableStateOf(false) }
        val menuYellow = remember { mutableStateOf(false) }
        val menuAll = remember { mutableStateOf(false) }
        if (isGreenPresent) {
            DropdownMenuItem(onClick = { menuGreen.value = !menuGreen.value }) {
                Text("Скопіювати зелені")
                DropdownMenu(
                    expanded = menuGreen.value,
                    onDismissRequest = { menuGreen.value = false }
                ) {
                    DropdownMenuItem(onClick = { viewModel.onCopyGreen() }) {
                        Text("Через пробіл")
                    }
                    DropdownMenuItem(onClick = { viewModel.onCopyGreenWithPorts() }) {
                        Text("Через пробіл з портами")
                    }
                    DropdownMenuItem(onClick = { viewModel.onCopyGreenCustom() }) {
                        Text("Свій формат")
                    }
                }
            }
        }
        if (isYellowPresent) {
            DropdownMenuItem(onClick = { menuYellow.value = !menuYellow.value }) {
                Text("Скопіювати жовті")
                DropdownMenu(
                    expanded = menuYellow.value,
                    onDismissRequest = { menuYellow.value = false }
                ) {
                    DropdownMenuItem(onClick = { viewModel.onCopyYellow() }) {
                        Text("Через пробіл")
                    }
                    DropdownMenuItem(onClick = { viewModel.onCopyYellowWithPorts() }) {
                        Text("Через пробіл з портами")
                    }
                    DropdownMenuItem(onClick = { viewModel.onCopyYellowCustom() }) {
                        Text("Свій формат")
                    }
                }
            }
        }
        Divider()
        DropdownMenuItem(onClick = { menuAll.value = !menuAll.value }) {
            Text("Скопіювати всі")
            DropdownMenu(
                expanded = menuAll.value,
                onDismissRequest = { menuAll.value = false }
            ) {
                DropdownMenuItem(onClick = { viewModel.onCopyAll() }) {
                    Text("Через пробіл")
                }
                DropdownMenuItem(onClick = { viewModel.onCopyAllWithPorts() }) {
                    Text("Через пробіл з портом")
                }
                DropdownMenuItem(onClick = { viewModel.onCopyAllCustom() }) {
                    Text("Свій формат")
                }
            }
        }
    }
}