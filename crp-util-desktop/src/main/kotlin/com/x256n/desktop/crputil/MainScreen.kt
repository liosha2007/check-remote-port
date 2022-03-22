package com.x256n.desktop.crputil

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.x256n.desktop.crputil.component.BottomPanelScreen
import com.x256n.desktop.crputil.component.ResultPanelScreen
import com.x256n.desktop.crputil.component.SourcePanelScreen

@Preview
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state
    Column(
        modifier = Modifier.fillMaxSize().padding(5.dp)
    ) {
        if (state.isInProgress) {
            ConsoleScreen(state)
        } else {
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                SourcePanelScreen(
                    modifier = if (state.activePanel == ActivePanel.SOURCE) Modifier.weight(1f) else Modifier.width(250.dp),
                    state = state,
                ) {
                    viewModel.onSourceChanged(it)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(15.dp)
                        .clickable {
                            viewModel.onActivePanelChange()
                        }
                )
                ResultPanelScreen(
                    modifier = if (state.activePanel == ActivePanel.RESULT) Modifier.weight(1f) else Modifier.width(250.dp),
                    viewModel,
                )
            }
            BottomPanelScreen(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                state = state,
                onTimeoutChange = viewModel::onTimeoutChange,
                onAnalizeClick = viewModel::onAnalizeClick
            )
        }
    }
}