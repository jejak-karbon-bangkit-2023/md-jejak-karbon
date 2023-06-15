package com.jejakkarbon.model

data class GuideDetail(
    val content: String,
    val image_url: String,
    val paragraphs: List<String>,
    val title: String
)