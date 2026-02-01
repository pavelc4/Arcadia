package com.android.arcadia.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.roundToInt

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ChartCard(
    title: String,
    data: List<Float>,
    color: Color,
    suffix: String = "",
    gridSteps: List<Float> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LineChart(
                data = data,
                modifier = Modifier.fillMaxWidth().weight(1f),
                graphColor = color,
                suffix = suffix,
                gridSteps = gridSteps
            )
        }
    }
}

/**
 * A custom Line Chart that supports touch interaction and grid lines.
 *
 * @param data List of float values to plot.
 * @param modifier Modifier for the canvas.
 * @param graphColor Color of the line and indicators.
 * @param suffix Optional suffix for the displayed value (e.g. " FPS").
 * @param gridSteps Optional list of Y-values to draw horizontal grid lines for.
 */
@Composable
fun LineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    graphColor: Color,
    suffix: String = "",
    gridSteps: List<Float> = emptyList()
) {
    if (data.isEmpty()) return

    var touchX by remember { mutableStateOf<Float?>(null) }
    var touchValue by remember { mutableStateOf<Float?>(null) }
    
    // Calculate range once
    val maxInit = data.maxOrNull() ?: 100f
    val minInit = data.minOrNull() ?: 0f
    
    // If grid steps are provided, ensure the graph covers them
    val maxVal = if (gridSteps.isNotEmpty()) maxOf(maxInit, gridSteps.max()) else maxInit
    val minVal = if (gridSteps.isNotEmpty()) minOf(minInit, gridSteps.min()) else minInit
    
    val range = maxVal - minVal
    val paddedRange = if (range == 0f) 1f else range * 1.2f
    val baseVal = minVal - (range * 0.1f) // Add slight padding below min

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val widthPerPoint = size.width / (data.size - 1)
                        val index = (offset.x / widthPerPoint).roundToInt().coerceIn(0, data.lastIndex)
                        touchX = offset.x
                        touchValue = data[index]
                        tryAwaitRelease()
                        touchX = null
                        touchValue = null
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        val widthPerPoint = size.width / (data.size - 1)
                        val index = (change.position.x / widthPerPoint).roundToInt().coerceIn(0, data.lastIndex)
                        touchX = change.position.x
                        touchValue = data[index]
                    },
                    onDragEnd = {
                        touchX = null
                        touchValue = null
                    },
                    onDragCancel = {
                        touchX = null
                        touchValue = null
                    }
                )
            }
    ) {
        val widthPerPoint = size.width / (data.size - 1)
        
        // 0. Draw Grid Lines (Background)
        if (gridSteps.isNotEmpty()) {
            val gridPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.GRAY
                alpha = 100 // Semi-transparent
                textSize = 30f
                textAlign = android.graphics.Paint.Align.LEFT
            }

            gridSteps.forEach { step ->
                if (step >= baseVal && step <= (baseVal + paddedRange)) {
                    val y = size.height - ((step - baseVal) / paddedRange) * size.height
                    
                    // Draw horizontal line
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                    
                    // Draw Label
                    drawContext.canvas.nativeCanvas.drawText(
                        "${step.toInt()}",
                        10.dp.toPx(), // Slight padding from left
                        y - 10f, // Slight padding above line
                        gridPaint
                    )
                }
            }
        }
        
        // 1. Draw the Graph Line
        val path = Path().apply {
            moveTo(0f, size.height - ((data.first() - baseVal) / paddedRange) * size.height)
            data.forEachIndexed { index, value ->
                val x = index * widthPerPoint
                val y = size.height - ((value - baseVal) / paddedRange) * size.height
                lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = graphColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // 2. Draw Touch Indicator and Value Text
        touchX?.let { tx ->
            val clampedX = tx.coerceIn(0f, size.width)
            
            // Draw dashed vertical line
            drawLine(
                color = Color.Gray,
                start = Offset(clampedX, 0f),
                end = Offset(clampedX, size.height),
                strokeWidth = 2f,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )

            // Draw data point circle
            touchValue?.let { value ->
                val widthIndex = (clampedX / widthPerPoint).roundToInt().coerceIn(0, data.lastIndex)
                val dotX = widthIndex * widthPerPoint
                val dotY = size.height - ((value - baseVal) / paddedRange) * size.height

                drawCircle(
                    color = graphColor,
                    radius = 6.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(dotX, dotY)
                )

                // Draw value text using native Canvas
                val text = "${value.toInt()}$suffix"
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 40f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                    setShadowLayer(5f, 0f, 0f, android.graphics.Color.BLACK)
                }
                
                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    clampedX,
                    30.dp.toPx(), // Position text at the top
                    paint
                )
            }
        }
    }
}
