package com.kndrd.android.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kndrd.android.feature.onboarding.OnboardingViewModel

@Composable
fun VerificationScreen(
    viewModel: OnboardingViewModel,
    onVerified: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Verify your number", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Enter the 6-digit code sent to ${state.phone}",
            style = MaterialTheme.typography.bodyMedium,
        )
        OutlinedTextField(
            value = state.verificationCode,
            onValueChange = { if (it.length <= 6) viewModel.updateCode(it) },
            label = { Text("Verification code") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = onVerified,
            enabled = state.verificationCode.length == 6,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        ) {
            Text("Verify")
        }
    }
}
