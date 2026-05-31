package com.kndrd.android.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(Route.Feed.path, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Route.CreatePlan.path, "Plan", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline),
    BottomNavItem(Route.Chats.path, "Chats", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    BottomNavItem(Route.Forum.path, "Forum", Icons.Filled.Forum, Icons.Outlined.Forum),
    BottomNavItem(Route.Profile.path, "Profile", Icons.Filled.Person, Icons.Outlined.Person),
)

@Composable
fun MainScaffold(
    feedScreen: @Composable () -> Unit,
    planDetailScreen: @Composable (planId: String) -> Unit,
    createPlanScreen: @Composable () -> Unit,
    chatsScreen: @Composable () -> Unit,
    chatDetailScreen: @Composable (roomId: String) -> Unit,
    forumScreen: @Composable () -> Unit,
    profileScreen: @Composable () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route?.let { route ->
        bottomNavItems.any { it.route == route } ||
                route == Route.Feed.path ||
                route == Route.Chats.path ||
                route == Route.Forum.path ||
                route == Route.Profile.path
    } ?: true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Feed.path,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Route.Feed.path) { feedScreen() }
            composable(Route.PlanDetail.path) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
                planDetailScreen(planId)
            }
            composable(Route.CreatePlan.path) { createPlanScreen() }
            composable(Route.Chats.path) { chatsScreen() }
            composable(Route.ChatDetail.path) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
                chatDetailScreen(roomId)
            }
            composable(Route.Forum.path) { forumScreen() }
            composable(Route.Profile.path) { profileScreen() }
        }
    }
}

fun NavController.navigateToPlanDetail(planId: String) =
    navigate(Route.PlanDetail.createRoute(planId))

fun NavController.navigateToChatDetail(roomId: String) =
    navigate(Route.ChatDetail.createRoute(roomId))
