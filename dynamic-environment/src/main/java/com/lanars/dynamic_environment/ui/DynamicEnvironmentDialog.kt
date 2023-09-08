package com.lanars.dynamic_environment.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lanars.dynamic_environment.interceptor.dynamicEnvironment
import com.lanars.dynamic_environment.interceptor.isCustomEnvironment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DynamicEnvironmentDialog(
    baseUrl: String,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences =
        remember { context.getSharedPreferences(dynamicEnvironment, Context.MODE_PRIVATE) }
    val environment =
        remember { mutableStateOf(sharedPreferences.getString(dynamicEnvironment, "")) }
    val isCustom =
        remember { mutableStateOf(sharedPreferences.getBoolean(isCustomEnvironment, false)) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(10),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Environment", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable {
                            isCustom.value = false
                        },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            RadioButton(
                                selected = !isCustom.value,
                                onClick = { isCustom.value = false },
                            )
                            Text(text = "Default")
                        }
                        Text(
                            text = baseUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable {
                            isCustom.value = true
                            coroutineScope.launch {
                                delay(50)
                                focusRequester.requestFocus()
                            }
                        },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            RadioButton(
                                selected = isCustom.value,
                                onClick = { isCustom.value = true },
                            )
                            Text(text = "Custom")
                        }

                        OutlinedTextField(
                            value = environment.value!!,
                            label = { Text("URL") },
                            placeholder = { Text("http(s)://domain.org:8000/") },
                            onValueChange = { environment.value = it },
                            singleLine = true,
                            enabled = isCustom.value,
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = onDismiss,
                        ) {
                            Text(
                                "Cancel",
                                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        TextButton(
                            onClick = {
                                sharedPreferences.edit()
                                    .putBoolean(isCustomEnvironment, isCustom.value).apply()
                                sharedPreferences.edit()
                                    .putString(dynamicEnvironment, environment.value).apply()
                                onDismiss()
                            }
                        ) {
                            Text(
                                "Save",
                                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

internal inline fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}