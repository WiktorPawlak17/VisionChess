package com.example.visionchess

import android.widget.Switch

data class Settings(
    val firstLaunch: Boolean,
    val sayPawn: Boolean,
    val sayTakes: Boolean,
    val sayPromotion: Boolean,
    val sayCheck: Boolean,
)

