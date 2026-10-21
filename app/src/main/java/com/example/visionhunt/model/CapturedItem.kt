package com.example.visionhunt.model

data class CapturedItem(
    val id: Long = 0,
    val taskId: String,
    val taskNameAr: String,
    val taskNameEn: String,
    val imagePath: String,
    val timestamp: Long,
    val score: Float
)
