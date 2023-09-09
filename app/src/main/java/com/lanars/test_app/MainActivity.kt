package com.lanars.test_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lanars.dynamic_environment.ui.DynamicEnvironmentDialog
import com.lanars.test_app.ui.theme.TestappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val dialogState = remember { mutableStateOf(false) }
                    Text(
                        text = "Open dialog",
                        modifier = Modifier.clickable {
                            dialogState.value = true
                        }
                    )

                    if (dialogState.value) {
                        DynamicEnvironmentDialog(
                            baseUrl = "https://vizhan.org/",
                            onDismiss = {
                                dialogState.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}