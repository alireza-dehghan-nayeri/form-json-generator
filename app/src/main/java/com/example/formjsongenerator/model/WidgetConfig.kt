package com.example.formjsongenerator.model

import com.example.formjsongenerator.model.DependencyConfig
import com.example.formjsongenerator.model.ValidationConfig


data class WidgetConfig(
    val id: Int,
    val type: String,
    val dataPath: String,
    val dependencies: List<DependencyConfig>,
    val hint: String?,
    val text: String?,
    val validations: List<ValidationConfig>
)

