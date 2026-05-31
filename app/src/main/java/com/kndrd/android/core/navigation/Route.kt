package com.kndrd.android.core.navigation

sealed class Route(val path: String) {
    // Graphs
    data object OnboardingGraph : Route("onboarding_graph")
    data object MainGraph : Route("main_graph")

    // Onboarding
    data object Welcome : Route("welcome")
    data object SignUp : Route("sign_up")
    data object Verification : Route("verification")
    data object InterestSelection : Route("interest_selection")

    // Main
    data object Feed : Route("feed")
    data object PlanDetail : Route("plan_detail/{planId}") {
        fun createRoute(planId: String) = "plan_detail/$planId"
    }
    data object CreatePlan : Route("create_plan")
    data object Chats : Route("chats")
    data object ChatDetail : Route("chat_detail/{roomId}") {
        fun createRoute(roomId: String) = "chat_detail/$roomId"
    }
    data object Forum : Route("forum")
    data object Profile : Route("profile")
}
