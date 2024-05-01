package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexaleluia12.somatruco2.R
import com.alexaleluia12.somatruco2.data.GameViewModel
import com.alexaleluia12.somatruco2.data.MenuOption
import com.alexaleluia12.somatruco2.ui.theme.Black
import com.alexaleluia12.somatruco2.ui.theme.Red

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
                    Text(stringResource(R.string.ok), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetAlert = false }) {
                    Text(stringResource(R.string.cancel), color = Color.White)
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
            name = appViewModel.editTarget?.name ?: "",
            onSaveName = {
                appViewModel.changeNameTo(it)
            },
            onDismiss = { canShowChangeNameDialog = false }
        )
    }

    ScreamMenu(
        triggerShowResetAlert = { showResetAlert = true },
        optA = MenuOption(
            showText = uiState.playerOne.name,
            triggerEditName = {
                appViewModel.editTarget = appViewModel.playerOne
                canShowChangeNameDialog = true
            },

            )
        , optB = MenuOption(
            showText = uiState.playerTwo.name,
            triggerEditName = {
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
            trigerSaveName = { canShowChangeNameDialog = true },
            setEditTarget = { appViewModel.editTarget = it },
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
            trigerSaveName = { canShowChangeNameDialog = true },
            setEditTarget = { appViewModel.editTarget = it }
        )
    }
}