package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexaleluia12.somatruco2.data.Player
import com.alexaleluia12.somatruco2.ui.theme.Typography

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier.fillMaxHeight(),
    points: Int = 0,
    wins: Int = 0,
    name: String = "P",
    color: Color,
    player: Player,
    onAddThree: () -> Unit,
    onAddOne: () -> Unit,
    onMinusOne: () -> Unit,
    trigerSaveName: () -> Unit,
    setEditTarget: (p: Player) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,) {
            Text(text = name, modifier = Modifier.clickable {
                setEditTarget(player)
                trigerSaveName()
            })
            Spacer(modifier = Modifier.size(28.dp))
            Text(
                text = points.toString(),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = Typography.titleLarge,
                modifier = Modifier
                    .size(120.dp)
                    .background(color = color, shape = CircleShape)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(18.dp))
            Text(
                text = wins.toString(),
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .size(70.dp)
                    .background(color = Color.Blue, shape = GoldenShape())
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )

        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MySquareButton(color = color, onClick = onAddThree) {
                Text("+3")
            }
            MySquareButton(color = color, onClick = onAddOne) {
                Text("+1")
            }
            MySquareButton(color = color, onClick = onMinusOne) {
                Text("-1")
            }

        }

    }
}