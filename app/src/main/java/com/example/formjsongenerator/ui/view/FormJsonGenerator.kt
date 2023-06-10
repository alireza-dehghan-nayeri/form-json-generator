package com.example.formjsongenerator.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun FormJsonGenerator(
    modifier: Modifier = Modifier,
    viewModel: ManiViewModel
) {

    val formConfig by viewModel.formConfig.collectAsState()
    var currentScreenIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomBar(
                onNextScreenClicked = {
                    if (currentScreenIndex == formConfig.screenConfigs.size - 1) {
                        viewModel.addNewScreen()
                    }
                    currentScreenIndex++

                },
                onPreviousScreenClicked = {
                    if (currentScreenIndex != 0) {
                        currentScreenIndex--
                    }
                },
                onSaveClicked = {
                    // todo: do the saving
                }
            )
        }
    ) { padding ->
        ScreenJsonGenerator(
            modifier = modifier.padding(padding),
            screenConfig = formConfig.screenConfigs[currentScreenIndex],
            onAddNewScreenDependencyConfig = {
                viewModel.addNewScreenDependencyConfig(it)
            },
            onAddNewWidgetConfig = {},
            onDeleteScreenConfig = {}
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onNextScreenClicked: () -> Unit,
    onPreviousScreenClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = {
            onNextScreenClicked()
        }) {
            Text(text = "next")
        }
        Button(onClick = {
            onSaveClicked()
        }) {
            Text(text = "save")
        }
        Button(onClick = {
            onPreviousScreenClicked()
        }) {
            Text(text = "previous")
        }
    }
}
