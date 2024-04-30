package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexaleluia12.somatruco2.R
import com.alexaleluia12.somatruco2.data.MenuOption

@Composable
fun ScreamMenu(
    triggerShowResetAlert: () -> Unit,
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
                triggerShowResetAlert()
            })
            DropdownMenuItem(
                text = {
                    Text(optA.showText)
                },
                onClick = {
                    expanded = false
                    optA.triggerEditName()
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
                    optB.triggerEditName()
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