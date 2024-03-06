package utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.sqrt

class MessageShape(
    private val radius: Float,
    private val cornerRadius: Float,
    private val heightArrow: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = Path().apply {
            star(
                size,
                radius,
                cornerRadius,
                heightArrow
            )
        })
    }

    private fun Path.star(size: Size, radius: Float, cornerRadius: Float, heightArrow: Float) {
        addRoundRect(
            RoundRect(
                size.copy(height = size.height - heightArrow).toRect(),
                CornerRadius(radius, radius)
            )
        )
        val top = size.height - heightArrow
        val start = size.width / 10 * 4 - 50f
        moveTo(start, top)
        lineTo(
            (start + 200f - cornerRadius + cornerRadius * (sqrt(2.0) / 2)).toFloat(),
            size.height
        )
        arcTo(
            Rect(Offset(start - cornerRadius + 200f, size.height - cornerRadius), cornerRadius),
            135f,
            -180f,
            true
        )
        cubicTo(
            (start + 200f + cornerRadius * (sqrt(2.0) / 2)).toFloat(),
            (size.height - cornerRadius * (sqrt(2.0) / 2)).toFloat(),
            start + 175f - cornerRadius * 2f, top + cornerRadius * 2f, start + 175f, top
        )
        lineTo(start + 50f, top)
        close()
    }
}