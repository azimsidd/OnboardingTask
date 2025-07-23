package com.thecodingshef.onboardingtask.core.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

object CommonUtil {

    fun String?.toComposeColorSafely(): Color {
        this ?: return Color.Transparent
        return try {
            val formattedColor = if (startsWith("#")) this else "#$this"
            Color(formattedColor.toColorInt())
        } catch (e: Exception) {
            Color.Black
        }
    }

}