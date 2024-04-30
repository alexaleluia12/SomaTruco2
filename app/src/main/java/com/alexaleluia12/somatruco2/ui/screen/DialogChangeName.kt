package com.alexaleluia12.somatruco2.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.alexaleluia12.somatruco2.R
import com.alexaleluia12.somatruco2.data.AppProperties

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