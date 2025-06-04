package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

enum class Avatars(val idAvatar: Int) {
    AVATAR1(R.drawable.avatar1), AVATAR2(R.drawable.avatar2_1), AVATAR3(R.drawable.avatar3);

    companion object {
        fun fromId(newIdAvatar: Int): Avatars =
            when(newIdAvatar) {
                R.drawable.avatar1 -> AVATAR1
                R.drawable.avatar2_1 -> AVATAR2
                R.drawable.avatar3 -> AVATAR3
                else -> AVATAR1
            }
    }
}