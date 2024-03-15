package com.alexaleluia12.somatruco2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexaleluia12.somatruco2.data.GameViewModel
import com.alexaleluia12.somatruco2.ui.theme.SomaTruco2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SomaTruco2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    appViewModel: GameViewModel = viewModel()
) {
    val uiState = appViewModel.uiState

    Row {
        Player(
            points = uiState.playerOne.count,
            wins = uiState.playerOne.winCount,
            onAddOne = appViewModel::incByOneForPlayerOne,
            onAddThree = appViewModel::incByThreeForPlayerOne,
            onMinusOne = appViewModel::decByOneForPlayerOne
        )
        Spacer(modifier = Modifier.width(16.dp))
        Player(
            points = uiState.playerTwo.count,
            wins = uiState.playerTwo.winCount,
            onAddOne = appViewModel::incByOneForPlayerTwo,
            onAddThree = appViewModel::incByThreeForPlayerTwo,
            onMinusOne = appViewModel::decByOneForPlayerTwo,
        )
    }

}

@Composable
fun Player(
    points: Int = 0,
    wins: Int = 0,
    onAddThree:() -> Unit,
    onAddOne: () -> Unit,
    onMinusOne: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row {
            Text(text = points.toString())
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            // TODO(btn deve se quadrado)
            Button(onClick = onAddThree) {
                Text("+3")
            }
            Button(onClick = onAddOne) {
                Text("+1")
            }

            Button(onClick = onMinusOne) {
                Text("-1")
            }
        }
        Button(onClick = {}) {
            Text(wins.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreemPreview() {
    SomaTruco2Theme {
        Screen()
    }
}