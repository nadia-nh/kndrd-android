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
import com.kndrd.android.feature.chatdetail.ui.ChatDetailScreen
import com.kndrd.android.feature.chats.ui.ChatsScreen
import com.kndrd.android.feature.createplan.ui.CreatePlanScreen
import com.kndrd.android.feature.feed.ui.FeedScreen
import com.kndrd.android.feature.forum.ui.ForumScreen
import com.kndrd.android.feature.plandetail.ui.PlanDetailScreen
import com.kndrd.android.feature.profile.ui.ProfileScreen

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
fun MainScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected =
                            navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Feed.path,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Route.Feed.path) {
                FeedScreen(
                    onPlanClick = { planId -> navController.navigateToPlanDetail(planId) },
                )
            }

            composable(Route.PlanDetail.path) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
                PlanDetailScreen(
                    planId = planId,
                    onBack = { navController.popBackStack() },
                    onJoined = { roomId ->
                        // Pop back to Feed, then open the chat room
                        navController.popBackStack(Route.Feed.path, inclusive = false)
                        navController.navigate(Route.Chats.path) {
                            launchSingleTop = true
                        }
                        navController.navigateToChatDetail(roomId)
                    },
                )
            }

            composable(Route.CreatePlan.path) {
                CreatePlanScreen(
                    onCreated = { navController.popBackStack() },
                )
            }

            composable(Route.Chats.path) {
                ChatsScreen(
                    onRoomClick = { roomId -> navController.navigateToChatDetail(roomId) },
                )
            }

            composable(Route.ChatDetail.path) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
                ChatDetailScreen(
                    roomId = roomId,
                    onBack = { navController.popBackStack() },
                )
            }

            composable(Route.Forum.path) { ForumScreen() }

            composable(Route.Profile.path) { ProfileScreen() }
        }
    }
}

fun NavController.navigateToPlanDetail(planId: String) =
    navigate(Route.PlanDetail.createRoute(planId))

fun NavController.navigateToChatDetail(roomId: String) =
    navigate(Route.ChatDetail.createRoute(roomId))
