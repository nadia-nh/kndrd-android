package com.kndrd.android.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kndrd.android.feature.chats.ui.ChatsScreen
import com.kndrd.android.feature.chatdetail.ui.ChatDetailScreen
import com.kndrd.android.feature.createplan.ui.CreatePlanScreen
import com.kndrd.android.feature.feed.ui.FeedScreen
import com.kndrd.android.feature.forum.ui.ForumScreen
import com.kndrd.android.feature.onboarding.OnboardingViewModel
import com.kndrd.android.feature.onboarding.ui.InterestSelectionScreen
import com.kndrd.android.feature.onboarding.ui.SignUpScreen
import com.kndrd.android.feature.onboarding.ui.VerificationScreen
import com.kndrd.android.feature.onboarding.ui.WelcomeScreen
import com.kndrd.android.feature.plandetail.ui.PlanDetailScreen
import com.kndrd.android.feature.profile.ui.ProfileScreen

@Composable
fun KndrdNavGraph(
    startDestination: String = Route.OnboardingGraph.path,
) {
    val rootNavController = rememberNavController()
    // Lock the start destination on first composition. Changes to startDestination
    // (e.g. from DataStore emitting while navigating) must not re-initialize NavHost.
    val initialStartDestination = remember { startDestination }

    NavHost(
        navController = rootNavController,
        startDestination = initialStartDestination,
    ) {
        navigation(
            route = Route.OnboardingGraph.path,
            startDestination = Route.Welcome.path,
        ) {
            composable(Route.Welcome.path) {
                val vm = hiltViewModel<OnboardingViewModel>(
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                )
                WelcomeScreen(
                    onGetStarted = { rootNavController.navigate(Route.SignUp.path) },
                    onSignIn = { rootNavController.navigate(Route.SignUp.path) },
                    onContinueAsGuest = {
                        vm.continueAsGuest {
                            rootNavController.navigate(Route.MainGraph.path) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                )
            }
            composable(Route.SignUp.path) {
                val vm = hiltViewModel<OnboardingViewModel>(
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                )
                SignUpScreen(
                    viewModel = vm,
                    onNext = { rootNavController.navigate(Route.Verification.path) },
                )
            }
            composable(Route.Verification.path) {
                val vm = hiltViewModel<OnboardingViewModel>(
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                )
                VerificationScreen(
                    viewModel = vm,
                    onVerified = { rootNavController.navigate(Route.InterestSelection.path) },
                )
            }
            composable(Route.InterestSelection.path) {
                val vm = hiltViewModel<OnboardingViewModel>(
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                )
                InterestSelectionScreen(
                    viewModel = vm,
                    onDone = {
                        rootNavController.navigate(Route.MainGraph.path) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                )
            }
        }

        composable(Route.MainGraph.path) {
            MainScaffold(
                feedScreen = {
                    FeedScreen(
                        onPlanClick = { planId ->
                            rootNavController.navigateToPlanDetail(planId)
                        }
                    )
                },
                planDetailScreen = { planId ->
                    PlanDetailScreen(
                        planId = planId,
                        onBack = { rootNavController.popBackStack() },
                        onJoined = { roomId ->
                            rootNavController.navigate(Route.MainGraph.path) {
                                popUpTo(Route.MainGraph.path) { inclusive = false }
                            }
                            rootNavController.navigateToChatDetail(roomId)
                        },
                    )
                },
                createPlanScreen = {
                    CreatePlanScreen(
                        onCreated = { rootNavController.popBackStack() },
                    )
                },
                chatsScreen = {
                    ChatsScreen(
                        onRoomClick = { roomId -> rootNavController.navigateToChatDetail(roomId) }
                    )
                },
                chatDetailScreen = { roomId ->
                    ChatDetailScreen(
                        roomId = roomId,
                        onBack = { rootNavController.popBackStack() },
                    )
                },
                forumScreen = { ForumScreen() },
                profileScreen = { ProfileScreen() },
            )
        }
    }
}
