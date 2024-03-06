package utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class StarShape(private val ends: Int, private val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        return Outline.Generic(path = Path().apply {
            star(
                ends,
                radius,
                size.center
            )

        })
    }

    private fun Path.star(ends: Int, radius: Float, center: Offset) {
        val innerRadius = radius / 3
        val outerRadius = radius / 2

        for (i in 0 until ends - 1) {
            val outerX = center.x + outerRadius * cos(i * 4 * PI / ((ends - 1) * 2))
            val outerY = center.y + outerRadius * sin(i * 4 * PI / ((ends - 1) * 2))

            if (i == 0) {
                moveTo(outerX.toFloat(), outerY.toFloat())
            } else {
                lineTo(outerX.toFloat(), outerY.toFloat())
            }

            val innerX = center.x + innerRadius * cos((i * 4 + 2) * PI / ((ends - 1) * 2))
            val innerY = center.y + innerRadius * sin((i * 4 + 2) * PI / ((ends - 1) * 2))

            lineTo(innerX.toFloat(), innerY.toFloat())
        }

        close()
    }
}