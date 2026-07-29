package com.halaqa.quran.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * مخططات بيانية مرسومة يدويًا عبر Compose Canvas — بدون أي مكتبة رسوم خارجية،
 * تخفيفًا لحجم التطبيق (نفس منطق الاختيار الذي طُبّق على PDF).
 */

data class BarChartEntry(val label: String, val value: Float, val maxValue: Float = 100f)

@Composable
fun SimpleBarChart(
    entries: List<BarChartEntry>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.fillMaxWidth().height(180.dp)) {
        if (entries.isEmpty()) return@Canvas
        val slotWidth = size.width / entries.size
        val barWidth = slotWidth * 0.5f
        val chartHeight = size.height - 24.dp.toPx()

        entries.forEachIndexed { index, entry ->
            val ratio = (entry.value / entry.maxValue).coerceIn(0f, 1f)
            val barHeight = chartHeight * ratio
            val left = index * slotWidth + (slotWidth - barWidth) / 2f
            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, chartHeight - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
        }
    }
}

@Composable
fun BarChartWithLabels(entries: List<BarChartEntry>, modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Column(modifier = modifier) {
        SimpleBarChart(entries)
        androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxWidth()) {
            entries.forEach { entry ->
                Text(
                    text = entry.label,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontSize = 11.sp, textAlign = TextAlign.Center)
                )
            }
        }
    }
}

/** مخطط خطي بسيط لعرض تطور الحفظ عبر أيام الأسبوع */
@Composable
fun SimpleLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.secondary
) {
    Canvas(modifier = modifier.fillMaxWidth().height(140.dp)) {
        if (values.size < 2) return@Canvas
        val maxVal = (values.maxOrNull() ?: 1f).coerceAtLeast(1f)
        val stepX = size.width / (values.size - 1)
        val points = values.mapIndexed { index, v ->
            Offset(index * stepX, size.height - (v / maxVal) * size.height)
        }
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }
        points.forEach { p ->
            drawCircle(color = lineColor, radius = 5f, center = p)
        }
    }
}
