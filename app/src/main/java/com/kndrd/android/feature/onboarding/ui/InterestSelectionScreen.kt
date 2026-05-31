package com.kndrd.android.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kndrd.android.core.model.Interest
import com.kndrd.android.feature.onboarding.OnboardingViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestSelectionScreen(
    viewModel: OnboardingViewModel,
    onDone: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("What are you into?", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Pick at least 3 interests to find your people.",
            style = MaterialTheme.typography.bodyMedium,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Interest.entries.forEach { interest ->
                val selected = interest in state.selectedInterests
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.toggleInterest(interest) },
                    label = { Text("${interest.emoji} ${interest.displayName}") },
                )
            }
        }
        Button(
            onClick = { viewModel.completeOnboarding(onDone) },
            enabled = state.selectedInterests.size >= 3 && !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Done")
        }
    }
}
