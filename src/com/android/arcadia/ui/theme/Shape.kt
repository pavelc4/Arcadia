package com.android.arcadia.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp


val ArcadiaShapes = Shapes(
    small = RoundedCornerShape(50), 
    medium = RoundedCornerShape(24.dp), 
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
)
