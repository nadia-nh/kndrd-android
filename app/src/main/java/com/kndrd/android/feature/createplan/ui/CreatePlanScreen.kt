package com.kndrd.android.feature.createplan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.kndrd.android.core.model.Interest
import com.kndrd.android.feature.createplan.CreatePlanViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanScreen(
    onCreated: () -> Unit,
    viewModel: CreatePlanViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var interestDropdownExpanded by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create a Hangout", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::updateDescription,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            ExposedDropdownMenuBox(
                expanded = interestDropdownExpanded,
                onExpandedChange = { interestDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = state.interest?.let { "${it.emoji} ${it.displayName}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Interest *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = interestDropdownExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = interestDropdownExpanded,
                    onDismissRequest = { interestDropdownExpanded = false },
                ) {
                    Interest.entries.forEach { interest ->
                        DropdownMenuItem(
                            text = { Text("${interest.emoji} ${interest.displayName}") },
                            onClick = {
                                viewModel.updateInterest(interest)
                                interestDropdownExpanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.location,
                onValueChange = viewModel::updateLocation,
                label = { Text("Location *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = state.dateTimeMs?.let { formatDateTimeMs(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Date & Time *") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) { Text("Pick") }
                },
            )

            Text("Max attendees: ${state.maxAttendees}", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = state.maxAttendees.toFloat(),
                onValueChange = { viewModel.updateMaxAttendees(it.toInt()) },
                valueRange = 4f..20f,
                steps = 15,
                modifier = Modifier.fillMaxWidth(),
            )

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = { viewModel.submit(onCreated) },
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text("Post Hangout")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                TimePicker(state = timePickerState)
                TextButton(
                    onClick = {
                        val dateMs = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = dateMs
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                        }
                        viewModel.updateDateTime(cal.timeInMillis)
                        showTimePicker = false
                    }
                ) { Text("OK") }
            }
        }
    }
}

private fun formatDateTimeMs(ms: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = ms }
    return buildString {
        append(java.text.SimpleDateFormat("EEE, MMM d", java.util.Locale.US).format(cal.time))
        append(" · ")
        append(java.text.SimpleDateFormat("h:mm a", java.util.Locale.US).format(cal.time))
    }
}
