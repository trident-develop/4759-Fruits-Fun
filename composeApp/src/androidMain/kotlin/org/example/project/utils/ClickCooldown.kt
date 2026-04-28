package org.example.project.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberCooldownClick(cooldownMs: Long = 1000L, onClick: () -> Unit): () -> Unit {
    val scope = rememberCoroutineScope()
    val enabled = remember { mutableStateOf(true) }
    return remember(onClick, cooldownMs) {
        {
            if (enabled.value) {
                enabled.value = false
                onClick()
                scope.launch {
                    delay(cooldownMs)
                    enabled.value = true
                }
            }
        }
    }
}
