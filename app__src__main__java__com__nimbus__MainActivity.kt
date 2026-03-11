package com.nimbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.nimbus.navigation.AppNavigation
import com.nimbus.ui.theme.NimbusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge display for immersive cloud experience
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NimbusTheme {
                AppNavigation()
            }
        }
    }
}
