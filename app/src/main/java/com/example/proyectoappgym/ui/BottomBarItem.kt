package com.example.proyectoappgym.ui

import androidx.compose.ui.graphics.vector.ImageVector

class BottomBarItem(
    val title: String,
    val icon: ImageVector,
    val selected: Boolean,
    val onClick: () -> Unit
)