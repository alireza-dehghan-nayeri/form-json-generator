package com.example.formjsongenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.formjsongenerator.model.DependencyConfig
import com.example.formjsongenerator.model.ScreenConfig
import com.example.formjsongenerator.model.WidgetConfig
import com.example.formjsongenerator.ui.theme.FormJsonGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ManiViewModel by viewModels()
        setContent {
            FormJsonGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FormJsonGenerator(viewModel = viewModel)
                }
            }
        }
    }
}

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
                Text(text = dependencyConfig.type)
                Button(onClick = { }) {
                    Text(text = "delete")
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "select type")
                Text(text = "select operator")
                Text(text = "select first operand")
                Text(text = "select second operand")
            }
        }
    }
    ItemSeparator(visible = expanded)

}

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

@Composable
fun ItemSeparator(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}


