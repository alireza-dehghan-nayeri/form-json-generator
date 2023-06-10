package com.example.formjsongenerator

import androidx.lifecycle.ViewModel
import com.example.formjsongenerator.data.FakeData
import com.example.formjsongenerator.model.DependencyConfig
import com.example.formjsongenerator.model.FormConfig
import com.example.formjsongenerator.model.InputJson
import com.example.formjsongenerator.model.ScreenConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class ManiViewModel : ViewModel() {

    private val _formConfig = MutableStateFlow(
        Gson().fromJson(FakeData.formConfigJson, InputJson::class.java).formConfig
    )
    val formConfig: StateFlow<FormConfig> = _formConfig

    private var lastScreenId: Int = _formConfig.value.screenConfigs.maxOfOrNull {
        it.id
    } ?: 0


    fun updateScreenDependencies(dependencies: List<DependencyConfig>, screenId: Int) {
        _formConfig.update {
            val screens = it.screenConfigs.toMutableList()
            val updatedScreen = screens.find { screen ->
                screen.id == screenId
            }?.copy(
                dependencies = dependencies
            )
            screens.removeIf { screen ->
                screen.id == screenId
            }
            updatedScreen?.let { screen ->
                screens.add(screen)
            }
            it.copy(screenConfigs = screens)
        }
    }

    fun updateWidgetConfig() {
    }

    fun addNewWidget() {
    }

    fun deleteWidget() {
    }

    fun deleteScreen() {
    }

    fun updateScreenConfig(screenConfig: ScreenConfig) {
        _formConfig.update {
            val screens = it.screenConfigs.toMutableList()
            screens.removeIf { screen ->
                screen.id == screenConfig.id
            }
            screens.add(screenConfig)
            it.copy(screenConfigs = screens)
        }
    }

    fun addNewScreen() {
        _formConfig.update {
            val screens = it.screenConfigs.toMutableList()
            lastScreenId++
            screens.add(
                ScreenConfig(
                    id = lastScreenId,
                    widgetConfigs = listOf(),
                    dependencies = listOf()
                )
            )
            it.copy(screenConfigs = screens)
        }
    }

    fun addNewScreenDependencyConfig(screenId: Int) {
        _formConfig.update {
            val screens = it.screenConfigs.toMutableList()
            val screen = screens.find { screen ->
                screen.id == screenId
            }
            val screenIndex = screens.indexOf(screen)
            screens.remove(screen)
            val screenDependencies = screen?.dependencies?.toMutableList()
            // todo: the type
            screenDependencies?.add(DependencyConfig(rule = mapOf(), type = ""))
            val updatedScreen = screenDependencies?.let { updatedDependencies ->
                screen.copy(dependencies = updatedDependencies)
            }
            updatedScreen?.let { theScreen -> screens.add(index = screenIndex, theScreen) }
            it.copy(screenConfigs = screens)
        }
    }
}

