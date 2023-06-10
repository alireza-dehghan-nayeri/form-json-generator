package com.example.formjsongenerator.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.formjsongenerator.model.ScreenConfig
import com.example.formjsongenerator.model.WidgetConfig

@Composable
fun ScreenJsonGenerator(
    modifier: Modifier = Modifier,
    screenConfig: ScreenConfig,
    onAddNewScreenDependencyConfig: (Int) -> Unit,
    onAddNewWidgetConfig: () -> Unit,
    onDeleteScreenConfig: () -> Unit
) {

    var expandedWidgetItem by remember { mutableStateOf<WidgetConfig?>(null) }

    Column {
        ScreenConfigItem(
            screenConfig = screenConfig,
            onAddNewScreenDependencyConfig = {
                onAddNewScreenDependencyConfig(it)
            },
            onAddNewWidgetConfig = onAddNewWidgetConfig,
            onDeleteScreenConfig = onDeleteScreenConfig
        )
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            modifier = modifier
        ) {
            // Widgets
            items(screenConfig.widgetConfigs) { widgetConfig ->
                WidgetConfigItem(
                    widgetConfig = widgetConfig,
                    expanded = expandedWidgetItem == widgetConfig,
                    onClick = {
                        expandedWidgetItem =
                            if (expandedWidgetItem == widgetConfig) null else widgetConfig
                    }
                )
            }

        }
    }
}

@Composable
fun ScreenConfigItem(
    screenConfig: ScreenConfig,
    onAddNewScreenDependencyConfig: (Int) -> Unit,
    onAddNewWidgetConfig: () -> Unit,
    onDeleteScreenConfig: () -> Unit
) {

    var expandScreenConfigItem by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Text(text = "Screen${screenConfig.id}")
            Row {
                Button(onClick = {
                    expandScreenConfigItem = !expandScreenConfigItem
                }) {
                    Text(text = "screen config")
                }
                Button(onClick = {
                    onAddNewWidgetConfig()
                }) {
                    Text(text = "add widget")
                }
                Button(onClick = {
                    onDeleteScreenConfig()
                }) {
                    Text(text = "delete screen")
                }
            }
            if (expandScreenConfigItem) {
                DependencyJsonGenerator(componentId = screenConfig.id,
                    dependencies = screenConfig.dependencies,
                    onAddNewScreenDependencyConfig = {
                        onAddNewScreenDependencyConfig(it)
                    }
                )
            }

        }
    }
}