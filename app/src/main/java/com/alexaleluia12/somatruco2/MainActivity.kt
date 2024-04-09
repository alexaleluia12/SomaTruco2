package com.alexaleluia12.somatruco2

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexaleluia12.somatruco2.data.AppProperties
import com.alexaleluia12.somatruco2.data.GameViewModel
import com.alexaleluia12.somatruco2.ui.theme.SomaTruco2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                    Text("Ok")
                }
            },
            text = { Text("Cuidado! esta de 11") }
        )
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
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetAlert = false }) {
                    Text("Cancelar")
                }
            },
            text = { Text("Deseja zerar todos pontos e voltar ao início") }
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
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
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
        )
        Spacer(modifier = Modifier.width(16.dp))

        Player(
            points = uiState.playerTwo.count,
            wins = uiState.playerTwo.winCount,
            name = uiState.playerTwo.name,
            onAddOne = appViewModel::incByOneForPlayerTwo,
            onAddThree = appViewModel::incByThreeForPlayerTwo,
            onMinusOne = appViewModel::decByOneForPlayerTwo,
            onSaveName = appViewModel::changeNamePlayerTwo,
        )


    }

}

// TODO(deixar botoes e texto maiores)
@Composable
fun Player(
    modifier: Modifier = Modifier.fillMaxHeight(),
    points: Int = 0,
    wins: Int = 0,
    name: String = "P",
    onSaveName: (name: String) -> Unit,
    onAddThree: () -> Unit,
    onAddOne: () -> Unit,
    onMinusOne: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {

        PlayerName(name = name, onSaveName =  onSaveName, finally = { })
        Text(text = points.toString())
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

        Button(onClick = {}) {
            Text(wins.toString())
        }
    }
}

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
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChangeNameDialog = false
                    finally()
                }) {
                    Text("Cancelar")
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

@Composable
fun ScreamMenu(
    reset: () -> Unit,
    optA:  @Composable (() -> Unit) -> Unit, // clear menu after complete edit a name
    optB:  @Composable (() -> Unit) -> Unit,
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
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Zerar tudo") }, onClick = {
                expanded = false
                reset()
            })
            DropdownMenuItem(text = {
                optA() { expanded = false }
            }, onClick = { }, leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = "editar") })

            DropdownMenuItem(text = {
                optB() { expanded = false }
            }, onClick = { }, leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = "editar") })
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