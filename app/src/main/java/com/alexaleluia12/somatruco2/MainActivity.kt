package com.alexaleluia12.somatruco2


import android.media.SoundPool
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexaleluia12.somatruco2.ui.screen.Screen
import com.alexaleluia12.somatruco2.ui.theme.SomaTruco2Theme

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


@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    SomaTruco2Theme {
        Screen()
    }
}