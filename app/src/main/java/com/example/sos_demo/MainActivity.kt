package com.example.sos_demo

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.sos_demo.ui.theme.SOS_DemoTheme
import android.Manifest

class MainActivity : ComponentActivity() {

    private var volumeUpPressed = false;
    private var volumeDownPressed = false;
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            SOS_DemoTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        Intent(applicationContext, VolumeButtonPressService::class.java).also {
                            it.action = VolumeButtonPressService.Actions.START.toString()
                            startService(it)
                        }
                    }) {
                        Text(text = "Start SOS listner")
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) : Boolean {
        if(keyCode === KeyEvent.KEYCODE_VOLUME_DOWN) {
             volumeDownPressed = true;
        } else if ( keyCode === KeyEvent.KEYCODE_VOLUME_UP) {
            volumeUpPressed = true;
        }

        // Cancel any pending reset and schedule a new reset after 1 second
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            volumeUpPressed = false
            volumeDownPressed = false
        }, 1000)

        if(volumeDownPressed && volumeUpPressed) {
            Toast.makeText(this, "Send SOS message", Toast.LENGTH_SHORT).show();
            volumeDownPressed = false;
            volumeUpPressed = false;
        }

        return super.onKeyDown(keyCode, event);
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Testing SOS Feature. Press Volume up and Volume down within 1 sec or simultaneously.",
            modifier = modifier
    )
}