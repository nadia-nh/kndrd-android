package com.kndrd.android.feature.plandetail.ui

import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kndrd.android.feature.plandetail.PlanDetailEvent
import com.kndrd.android.feature.plandetail.PlanDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    planId: String,
    onBack: () -> Unit,
    viewModel: PlanDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlanDetailEvent.NavigateToChat -> Unit // stay on screen after joining
                is PlanDetailEvent.LeftPlan -> onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.plan?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val plan = state.plan ?: return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SuggestionChip(
                onClick = {},
                label = { Text("${plan.interest.emoji} ${plan.interest.displayName}") },
            )

            Text(plan.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            // Host row
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = plan.hostPhotoUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Hosted by", style = MaterialTheme.typography.labelSmall)
                    Text(plan.hostName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }

            // Location — tapping opens Maps
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        val uri = Uri.parse("geo:0,0?q=${Uri.encode(plan.location)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    .padding(vertical = 4.dp),
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = "Open in Maps",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    plan.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            // Date/time — tapping opens Calendar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_INSERT).apply {
                            data = CalendarContract.Events.CONTENT_URI
                            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, plan.dateTimeMs)
                            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, plan.dateTimeMs + 2 * 3_600_000L)
                            putExtra(CalendarContract.Events.TITLE, plan.title)
                            putExtra(CalendarContract.Events.EVENT_LOCATION, plan.location)
                            putExtra(CalendarContract.Events.DESCRIPTION, plan.description)
                        }
                        context.startActivity(intent)
                    }
                    .padding(vertical = 4.dp),
            ) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = "Add to Calendar",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    SimpleDateFormat("EEEE, MMMM d · h:mm a", Locale.US).format(Date(plan.dateTimeMs)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Group, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "${plan.currentAttendees} going · ${plan.maxAttendees - plan.currentAttendees} spots left",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(plan.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(8.dp))

            if (plan.isJoined) {
                // "You're going" indicator
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                ) {
                    Text("You're going ✓")
                }
                // Leave plan
                OutlinedButton(
                    onClick = viewModel::leavePlan,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLeaving,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    if (state.isLeaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    } else {
                        Text("Leave plan")
                    }
                }
            } else {
                Button(
                    onClick = viewModel::joinPlan,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isJoining,
                ) {
                    if (state.isJoining) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Join Plan")
                    }
                }
            }
        }
    }
}
