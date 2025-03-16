package com.jeongu.barointernapp.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jeongu.barointernapp.presentation.theme.DarkGreen
import com.jeongu.barointernapp.presentation.theme.Green
import com.jeongu.barointernapp.presentation.theme.Orange
import com.jeongu.barointernapp.presentation.theme.Red

@Composable
fun getTemperatureColor(temperature: Double): Color {
    return when {
        temperature >= 70 -> Green
        temperature >= 50 -> DarkGreen
        temperature >= 30 -> Orange
        else -> Red
    }
}