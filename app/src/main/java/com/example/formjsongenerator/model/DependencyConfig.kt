package com.example.formjsongenerator.model

data class DependencyConfig(
    val rule: Map<String, List<Any>>,
    val type: String
)