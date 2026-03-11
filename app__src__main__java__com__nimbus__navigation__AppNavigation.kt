package com.nimbus.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.nimbus.ui.screens.*
import com.nimbus.ui.theme.*

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home   : Screen("home",   "Focus",  Icons.Rounded.Timer)
    object Breathe: Screen("breathe","Breathe",Icons.Rounded.Air)
    object Stats  : Screen("stats",  "Stats",  Icons.Rounded.BarChart)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val screens = listOf(Screen.Home, Screen.Breathe, Screen.Stats)

    Scaffold(
        containerColor = CloudWhite,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(screen.icon, contentDescription = screen.label,
                                tint = if (selected) DeepSky else NightSky.copy(alpha = 0.4f))
                        },
                        label = {
                            Text(screen.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (selected) DeepSky else NightSky.copy(alpha = 0.4f)
                                ))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MistBlue.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(220)) + slideInHorizontally { it / 6 } },
            exitTransition  = { fadeOut(tween(180)) }
        ) {
            composable(Screen.Home.route)    { HomeScreen() }
            composable(Screen.Breathe.route) { BreatheScreen() }
            composable(Screen.Stats.route)   { StatsScreen() }
        }
    }
}
