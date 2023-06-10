package com.example.formjsongenerator.model


data class ValidationConfig(
    val type: String,
    val message: String,
    val dependencies: List<DependencyConfig>
)
