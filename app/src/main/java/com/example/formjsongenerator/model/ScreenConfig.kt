package com.example.formjsongenerator.model


data class ScreenConfig(
    val id: Int,
    val widgetConfigs: List<WidgetConfig>,
    val dependencies: List<DependencyConfig>
)


