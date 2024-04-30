package com.alexaleluia12.somatruco2


import android.media.SoundPool
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexaleluia12.somatruco2.data.AppProperties
import com.alexaleluia12.somatruco2.data.GameViewModel
import com.alexaleluia12.somatruco2.data.Player
import com.alexaleluia12.somatruco2.ui.theme.Black
import com.alexaleluia12.somatruco2.ui.theme.Red
import com.alexaleluia12.somatruco2.ui.theme.SomaTruco2Theme
import com.alexaleluia12.somatruco2.ui.theme.Typography

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val sound = SoundPool.Builder().setMaxStreams(1).build()
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
    // TODO(verificar se erro existe em outros apps de referencia)
    // se for Viavel = resolver erro qnd clica num dialog, esta acontecendo com todos
    // ? sera que um erro desse eh critico o suficiente para impedir de por na loja
    // consigo rodar outros app com composa para verificar, todas dependencias estao atualizadas
    // InputDispatcher         system_server    E  Window handle Window{ccfa8e2 u0 com.alexaleluia12.somatruco2/com.alexaleluia12.somatruco2.MainActivity} has no registered input channel
    LaunchedEffect(uiState.playerOne.closeToWin, uiState.playerTwo.closeToWin) {
        canShowAlert = uiState.playerOne.closeToWin || uiState.playerTwo.closeToWin
    }

    if (canShowAlert) {
        Dialog(
            onDismissRequest = {
                canShowAlert = false
            },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.breakthrow_point),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
        SideEffect {
            playSound()
        }

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

    var canShowChangeNameDialog by remember {
        mutableStateOf(false)
    }
    if (canShowChangeNameDialog) {
        DialogChangeName(
            name = appViewModel.editTarget?.name ?: "" ,
            onSaveName = {
                appViewModel.changeNameTo(it)
            },
            onDismiss = { canShowChangeNameDialog = false }
        )
    }

    ScreamMenu(
        trigerShowResetAlert = { showResetAlert = true },
        optA =
            MenuOption(
                showText = uiState.playerOne.name,
                trigerEditName = {
                    appViewModel.editTarget = appViewModel.playerOne
                    canShowChangeNameDialog = true
                },

            )
        , optB =
            MenuOption(
                showText = uiState.playerTwo.name,
                trigerEditName = {
                    appViewModel.editTarget = appViewModel.playerTwo
                    canShowChangeNameDialog = true
                },
            )
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        PlayerScreen(
            points = uiState.playerOne.count,
            wins = uiState.playerOne.winCount,
            name = uiState.playerOne.name,
            player = appViewModel.playerOne,
            onAddOne = appViewModel::incByOneForPlayerOne,
            onAddThree = appViewModel::incByThreeForPlayerOne,
            onMinusOne = appViewModel::decByOneForPlayerOne,
            color = Black,
            trigerSaveName = {canShowChangeNameDialog = true},
            setEditTarget = {appViewModel.editTarget = it},
        )

        PlayerScreen(
            points = uiState.playerTwo.count,
            wins = uiState.playerTwo.winCount,
            name = uiState.playerTwo.name,
            player = appViewModel.playerTwo,
            onAddOne = appViewModel::incByOneForPlayerTwo,
            onAddThree = appViewModel::incByThreeForPlayerTwo,
            onMinusOne = appViewModel::decByOneForPlayerTwo,
            color = Red,
            trigerSaveName = {canShowChangeNameDialog = true},
            setEditTarget  = {appViewModel.editTarget = it}
        )


    }

}


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
        Column(modifier.weight(1f),  horizontalAlignment = Alignment.CenterHorizontally,) {
            Text(text=name, modifier = Modifier.clickable {
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
            verticalArrangement = Arrangement.SpaceBetween) {
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

// TODO(mover alguns partes para fora do MainActivity)
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

// TODO(dialogo no tema escuro ta muito ruim)
@Composable
fun DialogChangeName(name: String, onSaveName: (name: String) -> Unit, onDismiss: () -> Unit) {
    var tmpName by remember { mutableStateOf(name) }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(onClick = {
                onSaveName(tmpName)
                onDismiss()
            }) {
                Text(stringResource(R.string.save), color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(stringResource(R.string.cancel), color = Color.White)
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

data class MenuOption(val showText: String, val trigerEditName: () -> Unit)

@Composable
fun ScreamMenu(
    trigerShowResetAlert: () -> Unit,
    optA: MenuOption,
    optB: MenuOption,
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd),
        contentAlignment = Alignment.TopEnd
    )
    {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.description_menu)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(stringResource(R.string.clear_msg)) }, onClick = {
                expanded = false
                trigerShowResetAlert()
            })
            DropdownMenuItem(
                text = {
                    Text(optA.showText)
                },
                onClick = {
                    expanded = false
                    optA.trigerEditName()
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.description_edit)
                    )
                })

            DropdownMenuItem(
                text = {
                    Text(optB.showText)
                },
                onClick = {
                    expanded = false
                    optB.trigerEditName()
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.description_edit)
                    )
                })
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