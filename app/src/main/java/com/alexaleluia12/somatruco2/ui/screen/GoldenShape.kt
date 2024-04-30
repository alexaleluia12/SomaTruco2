package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class GoldenShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        return Outline.Generic(
            path = rotatedSquare(size = size)
        )
    }

    private fun rotatedSquare(size: Size): Path {
        val path = Path()
        path.moveTo(size.width / 2f, size.height)
        path.lineTo(size.width, size.height / 2f)

        path.lineTo(size.width / 2f, 0f)
        path.lineTo(0f, size.height / 2f)

        path.close()

        return path
    }

}