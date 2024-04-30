package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun MySquareButton(color: Color, onClick: () -> Unit, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier.size(70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        content()
    }
}