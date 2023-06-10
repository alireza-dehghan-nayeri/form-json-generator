package com.example.formjsongenerator.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.formjsongenerator.model.DependencyConfig
import com.example.formjsongenerator.ui.theme.FormJsonGeneratorTheme

@Composable
fun DependencyJsonGenerator(
    componentId: Int,
    dependencies: List<DependencyConfig>,
    onAddNewScreenDependencyConfig: (Int) -> Unit
) {

    var expandedDependencyItem by remember { mutableStateOf<DependencyConfig?>(null) }

    Column {
        Text(text = "dependencies ${dependencies.size}")
        Row {
            Button(onClick = {
                onAddNewScreenDependencyConfig(componentId)
            }) {
                Text(text = "add")
            }

        }
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 32.dp
            ),
        ) {
            items(dependencies) {
                DependencyConfigItem(dependencyConfig = it,
                    expanded = expandedDependencyItem == it,
                    onClick = {
                        expandedDependencyItem =
                            if (expandedDependencyItem == it) null else it
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DependencyConfigItem(
    dependencyConfig: DependencyConfig,
    expanded: Boolean,
    onClick: () -> Unit
) {

    var typeDropdownExpanded by remember {
        mutableStateOf(false)
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = dependencyConfig.type)
                Button(onClick = { }) {
                    Text(text = "delete")
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(expanded = typeDropdownExpanded, onExpandedChange = {
                    typeDropdownExpanded = !typeDropdownExpanded
                }) {

                }
                Text(text = "select type")
                Text(text = "select operator")
                Text(text = "select first operand")
                Text(text = "select second operand")
            }
        }
    }
    ItemSeparator(visible = expanded)

}

@Preview(showBackground = true)
@Composable
fun DependencyJsonGeneratorPreview() {

}

@Preview(showBackground = true)
@Composable
fun DependencyConfigItemPreview() {
    var expanded by remember {
        mutableStateOf(true)
    }
    FormJsonGeneratorTheme {
        Surface {
            DependencyConfigItem(
                dependencyConfig = DependencyConfig(rule = mapOf(), type = "VISIBILITY"),
                expanded = expanded
            ) {
                expanded = !expanded
            }
        }
    }
}

