package com.alexaleluia12.somatruco2


import android.media.SoundPool
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexaleluia12.somatruco2.data.AppProperties
import com.alexaleluia12.somatruco2.data.GameViewModel
import com.alexaleluia12.somatruco2.ui.theme.Black
import com.alexaleluia12.somatruco2.ui.theme.Red
import com.alexaleluia12.somatruco2.ui.theme.SomaTruco2Theme
import com.alexaleluia12.somatruco2.ui.theme.Typography

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val sound = SoundPool.Builder().setMaxStreams(1).build()
        // TODO(usar lazy inity)
        val soundId = sound.load(applicationContext, R.raw.bit_sound, 1)


        setContent {
            SomaTruco2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(playSound = {
                        sound.play(soundId, 0.4f, 0.4f, 0,1,1.0f)
                    })
                }
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    appViewModel: GameViewModel = viewModel(),
    playSound: () -> Unit = {},
) {
    val uiState = appViewModel.uiState
    var canShowAlert by remember {
        mutableStateOf(false)
    }
    // TODO(resolver erro qnd mostra Alerta)
    // InputDispatcher         system_server    E  Window handle Window{ccfa8e2 u0 com.alexaleluia12.somatruco2/com.alexaleluia12.somatruco2.MainActivity} has no registered input channel
    LaunchedEffect(uiState.playerOne, uiState.playerTwo) {
        // esta quase do jeito que eu quero
        // lanca o sinal so uma vez, enquanto o outro jogadr incrementa o numero dele
        // nao deve mostrar novamente anão ser que ele chege a 11 tmb
        if (uiState.playerOne.count == 11 || uiState.playerTwo.count == 11) {
            canShowAlert = true
        }
    }

    if (canShowAlert) {
        // tmb quero mostrar um sinal sonoro
        AlertDialog(
            onDismissRequest = {
                canShowAlert = false
            },
            confirmButton = {
                TextButton(onClick = { canShowAlert = false }) {
                    Text(stringResource(R.string.ok))
                }
            },
            text = { Text(stringResource(R.string.breakthrow_point)) }
        )
        playSound()
    }
    var showResetAlert by remember { mutableStateOf(false) }

    if (showResetAlert) {
        AlertDialog(
            onDismissRequest = {
                showResetAlert = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showResetAlert = false
                    appViewModel.reset()
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetAlert = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            text = { Text(stringResource(R.string.confir_reset_msg)) }
        )
    }


    ScreamMenu(
        reset = { showResetAlert = true },
        optA = {
            PlayerName(
                name = uiState.playerOne.name,
                onSaveName = appViewModel::changeNamePlayerOne,
                finally = it
            )
        }, optB = {
            PlayerName(
                name = uiState.playerTwo.name,
                onSaveName = appViewModel::changeNamePlayerTwo,
                finally = it
            )
        }
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Player(
            points = uiState.playerOne.count,
            wins = uiState.playerOne.winCount,
            name = uiState.playerOne.name,
            // TODO(resolver) - aparece qnd aperta em +1
            // ForceDarkHelper  com.alexaleluia12.somatruco2  D  updateByCheckExcludeList:
            onAddOne = appViewModel::incByOneForPlayerOne,
            onAddThree = appViewModel::incByThreeForPlayerOne,
            onMinusOne = appViewModel::decByOneForPlayerOne,
            onSaveName = appViewModel::changeNamePlayerOne,
            color = Black,
        )
        //Spacer(modifier = Modifier.width(16.dp))

        Player(
            points = uiState.playerTwo.count,
            wins = uiState.playerTwo.winCount,
            name = uiState.playerTwo.name,
            onAddOne = appViewModel::incByOneForPlayerTwo,
            onAddThree = appViewModel::incByThreeForPlayerTwo,
            onMinusOne = appViewModel::decByOneForPlayerTwo,
            onSaveName = appViewModel::changeNamePlayerTwo,
            color = Red,
        )


    }

}


@Composable
fun Player(
    modifier: Modifier = Modifier.fillMaxHeight(),
    points: Int = 0,
    wins: Int = 0,
    name: String = "P",
    color: Color,
    onSaveName: (name: String) -> Unit,
    onAddThree: () -> Unit,
    onAddOne: () -> Unit,
    onMinusOne: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier.weight(1f),  horizontalAlignment = Alignment.CenterHorizontally,) {
            PlayerName(name = name, onSaveName = onSaveName, finally = { })
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
            verticalArrangement = Arrangement.SpaceBetween) {


            Button(
                onClick = onAddThree,
                shape = RectangleShape,
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = Color.White
                ),
            ) {
                Text("+3")
            }
            Button(
                onClick = onAddOne,
                shape = RectangleShape,
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = Color.White
                ),
            ) {
                Text("+1")
            }

            Button(
                onClick = onMinusOne,
                shape = RectangleShape,
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = Color.White
                )
            ) {
                Text("-1")
            }
        }

    }
}

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
// TODO(implementar outra forma de mudar o nome do jogador, de forma mais desacoplada)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerName(name: String, onSaveName: (name: String) -> Unit, finally: () -> Unit) {
    var showChangeNameDialog by remember { mutableStateOf(false) }

    TextButton(onClick = { showChangeNameDialog = true }) {
        Text(name)
    }
    var tmpName by remember { mutableStateOf(name) }

    if (showChangeNameDialog) {
        AlertDialog(
            onDismissRequest = {
                showChangeNameDialog = false
                finally()
            },
            confirmButton = {
                TextButton(onClick = {
                    showChangeNameDialog = false
                    onSaveName(tmpName)
                    finally()
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChangeNameDialog = false
                    finally()
                }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(
                    stringResource(
                        R.string.availableSpace,
                        AppProperties.MAX_NAME_LENGTH - tmpName.length
                    )
                )
            },
            text = {
                TextField(value = tmpName, onValueChange = {
                    if (it.length <= AppProperties.MAX_NAME_LENGTH) {
                        tmpName = it
                    }
                })
            }
        )
    }
}
//TODO(avaliar se eh possivel apenas chamar as funcoes de edit emvez de criar PlayerName)
@Composable
fun ScreamMenu(
    reset: () -> Unit,
    optA: @Composable (() -> Unit) -> Unit, // clear menu after complete edit a name
    optB: @Composable (() -> Unit) -> Unit,
) {
    // nova callback passa clear e retorna composable
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd),
        contentAlignment = Alignment.TopEnd
    )
    {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.description_menu))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(stringResource(R.string.clear_msg)) }, onClick = {
                expanded = false
                reset()
            })
            DropdownMenuItem(
                text = {
                    optA() { expanded = false }
                },
                onClick = { },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.description_edit)) })

            DropdownMenuItem(
                text = {
                    optB() { expanded = false }
                },
                onClick = { },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.description_edit)) })
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