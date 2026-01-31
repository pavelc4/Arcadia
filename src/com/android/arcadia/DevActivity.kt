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
import com.android.arcadia.ui.ArcadiaSettingsScreen
import com.android.arcadia.ui.theme.ArcadiaTheme

class DevActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        
        setContent {
            ArcadiaTheme {
                ArcadiaSettingsScreen()
            }
        }
    }
}
