package utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.sqrt

class EndMessageShape(
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

    private fun Path.star(size: Size, radius: Float, cornerRadius: Float, widthArrow: Float) {
        addRoundRect(
            RoundRect(
                size.copy(width = size.width - widthArrow).toRect(),
                CornerRadius(radius, radius)
            )
        )
        val end = size.width - widthArrow
        val bottom = radius + size.height / 20
        moveTo(end, bottom)
        lineTo(
            size.width,
            radius + size.height / 10 * 2.5f
        )
        lineTo(end,radius + size.height / 10 * 2f)
        close()
    }
}