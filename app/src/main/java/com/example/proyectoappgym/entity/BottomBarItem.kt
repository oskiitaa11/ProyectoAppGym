package com.example.proyectoappgym.entity

import androidx.compose.ui.graphics.vector.ImageVector

class BottomBarItem(
    val title: String,
    val iconSelectedBottom: ImageVector,
    val iconBottom: ImageVector,
    val selected: Boolean,
    val onClick: () -> Unit
)