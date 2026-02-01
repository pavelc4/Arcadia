package com.android.arcadia

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.arcadia.ui.PerAppScreen
import com.android.arcadia.ui.SystemMonitorScreen
import com.android.arcadia.ui.LogDetailScreen
import com.android.arcadia.ui.ArcadiaSettingsScreen
import com.android.arcadia.ui.theme.ArcadiaTheme
import androidx.compose.runtime.*

class DevActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        
        setContent {
            ArcadiaTheme {
                var currentScreen by remember { mutableStateOf("settings") }
                
                when (currentScreen) {
                    "settings" -> ArcadiaSettingsScreen(
                        onPerAppClick = { currentScreen = "per_app" },
                        onSystemMonitorClick = { currentScreen = "system_monitor" }
                    )
                    "per_app" -> PerAppScreen(
                        onBack = { currentScreen = "settings" }
                    )
                    "system_monitor" -> SystemMonitorScreen(
                        onBack = { currentScreen = "settings" },
                        onAppClick = { packageName -> currentScreen = "log_detail/$packageName" }
                    )
                    else -> {
                        if (currentScreen.startsWith("log_detail/")) {
                            val packageName = currentScreen.removePrefix("log_detail/")
                            LogDetailScreen(
                                packageName = packageName,
                                onBack = { currentScreen = "system_monitor" }
                            )
                        } else {
                            // Fallback
                            currentScreen = "settings"
                        }
                    }
                }
            }
        }
    }
}
