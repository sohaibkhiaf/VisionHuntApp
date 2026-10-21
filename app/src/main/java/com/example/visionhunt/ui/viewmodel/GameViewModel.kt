package com.example.visionhunt.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.visionhunt.data.CocoTasks
import com.example.visionhunt.data.GameRepository
import com.example.visionhunt.model.Detection
import com.example.visionhunt.model.GameProgress
import com.example.visionhunt.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepository(application)

    val gameState: StateFlow<GameState> = repository.gameState

    private val _gameProgress = MutableStateFlow(repository.getProgress())
    val gameProgress: StateFlow<GameProgress> = _gameProgress.asStateFlow()

    private val _rawDetections = MutableStateFlow<List<Detection>>(emptyList())
    val rawDetections: StateFlow<List<Detection>> = _rawDetections.asStateFlow()

    private val _gameDetections = MutableStateFlow<List<Detection>>(emptyList())
    val gameDetections: StateFlow<List<Detection>> = _gameDetections.asStateFlow()

    private val _inferenceError = MutableStateFlow<String?>(null)
    val inferenceError: StateFlow<String?> = _inferenceError.asStateFlow()

    private val _inferenceTime = MutableStateFlow(0L)
    val inferenceTime: StateFlow<Long> = _inferenceTime.asStateFlow()

    private val _isModelLoaded = MutableStateFlow(true)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded.asStateFlow()

    fun onDetectionsReceived(results: List<Detection>, inferenceTimeMs: Long) {
        _rawDetections.value = results
        _inferenceTime.value = inferenceTimeMs
        _inferenceError.value = null

        val currentCompleted = gameState.value.completedTaskIds

        val matchingTasks = results.mapNotNull { detection ->
            val task = CocoTasks.TASK_BY_LABEL[detection.label.trim().lowercase()]
            if (task != null && detection.score >= task.confidenceThreshold) {
                task to detection
            } else null
        }

        _gameDetections.value = matchingTasks.map { it.second }

        var progressChanged = false
        for ((task, _) in matchingTasks) {
            if (task.id !in currentCompleted) {
                if (repository.completeTask(task.id)) {
                    progressChanged = true
                }
            }
        }

        if (progressChanged || _gameProgress.value.score != repository.getProgress().score) {
            _gameProgress.value = repository.getProgress()
        }
    }

    fun onDetectorError(error: String) {
        _inferenceError.value = error
        _isModelLoaded.value = false
    }

    fun resetGame() {
        repository.resetGame()
        _gameProgress.value = repository.getProgress()
        _rawDetections.value = emptyList()
        _gameDetections.value = emptyList()
    }

    fun completeTask(taskId: String) {
        if (repository.completeTask(taskId)) {
            _gameProgress.value = repository.getProgress()
        }
    }
}
