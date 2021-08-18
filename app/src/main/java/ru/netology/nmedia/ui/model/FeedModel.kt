package ru.netology.nmedia.ui.model

import ru.netology.nmedia.ui.dto.Marker

data class FeedModel(
    val markers: List<Marker> = emptyList(),
    val empty: Boolean = false,
    val error: Boolean = false,
)
