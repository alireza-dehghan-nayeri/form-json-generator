package com.example.formjsongenerator.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.formjsongenerator.model.WidgetConfig

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WidgetConfigItem(widgetConfig: WidgetConfig, expanded: Boolean, onClick: () -> Unit) {
    ItemSeparator(visible = expanded)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row {
                Text(text = widgetConfig.type)
                Button(onClick = { }) {
                    Text(text = "delete")
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "select type")
                Text(text = "select data path")
                Text(text = "select dependencies")
                Text(text = "select validations")
                Text(text = "select widget specific type related config")
            }
        }
    }
    ItemSeparator(visible = expanded)
}