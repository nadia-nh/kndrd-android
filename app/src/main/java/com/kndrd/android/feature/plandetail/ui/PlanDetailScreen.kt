package com.kndrd.android.feature.plandetail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Placeholder — replaced in Sprint 5
@Composable
fun PlanDetailScreen(
    planId: String,
    onBack: () -> Unit,
    onJoined: (roomId: String) -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Plan Detail: $planId")
    }
}
