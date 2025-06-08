package com.example.proyectoappgym.entity

import com.example.proyectoappgym.R

enum class Avatars(val idAvatar:Int, val idAvatarImage: Int) {
    AVATAR1(1, R.drawable.avatar1), AVATAR2(2, R.drawable.avatar2_1), AVATAR3(3, R.drawable.avatar3);

    companion object {
        fun fromId(newIdAvatarImage: Int): Avatars =
            when(newIdAvatarImage) {
                R.drawable.avatar1 -> AVATAR1
                R.drawable.avatar2_1 -> AVATAR2
                R.drawable.avatar3 -> AVATAR3
                else -> AVATAR1
            }

        fun toAvatar(idAvatar: Int) =
            when(idAvatar) {
                1 -> AVATAR1
                2 -> AVATAR2
                3 -> AVATAR3
                else -> AVATAR1
            }

    }

}