package com.kndrd.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kndrd.android.core.navigation.KndrdNavGraph
import com.kndrd.android.core.navigation.MainViewModel
import com.kndrd.android.core.navigation.Route
import com.kndrd.android.core.ui.theme.KndrdTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KndrdTheme {
                val isOnboardingComplete by mainViewModel.isOnboardingComplete.collectAsStateWithLifecycle()
                val startDestination = if (isOnboardingComplete) {
                    Route.MainGraph.path
                } else {
                    Route.OnboardingGraph.path
                }
                KndrdNavGraph(startDestination = startDestination)
            }
        }
    }
}
