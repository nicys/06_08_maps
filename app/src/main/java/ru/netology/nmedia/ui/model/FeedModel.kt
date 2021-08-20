package ru.netology.nmedia.ui.model

import ru.netology.nmedia.ui.dto.Place

data class FeedModel(
    val markers: List<Place> = emptyList(),
    val empty: Boolean = false,
    val error: Boolean = false
)