package com.kndrd.android.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kndrd.android.feature.chatdetail.ui.ChatDetailScreen
import com.kndrd.android.feature.chats.ui.ChatsScreen
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
            composable(Route.Welcome.path) { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                }
                val vm = hiltViewModel<OnboardingViewModel>(onboardingEntry)
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
            composable(Route.SignUp.path) { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                }
                val vm = hiltViewModel<OnboardingViewModel>(onboardingEntry)
                SignUpScreen(
                    viewModel = vm,
                    onNext = { rootNavController.navigate(Route.Verification.path) },
                )
            }
            composable(Route.Verification.path) { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                }
                val vm = hiltViewModel<OnboardingViewModel>(onboardingEntry)
                VerificationScreen(
                    viewModel = vm,
                    onVerified = { rootNavController.navigate(Route.InterestSelection.path) },
                )
            }
            composable(Route.InterestSelection.path) { backStackEntry ->
                val onboardingEntry = remember(backStackEntry) {
                    rootNavController.getBackStackEntry(Route.OnboardingGraph.path)
                }
                val vm = hiltViewModel<OnboardingViewModel>(onboardingEntry)
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
