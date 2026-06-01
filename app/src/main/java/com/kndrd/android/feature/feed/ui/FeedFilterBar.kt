package com.kndrd.android.feature.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kndrd.android.core.model.Interest
import com.kndrd.android.feature.feed.FeedStatusFilter

@Composable
fun FeedFilterBar(
    selectedStatus: FeedStatusFilter,
    onStatusSelected: (FeedStatusFilter) -> Unit,
    selectedInterest: Interest?,
    onInterestSelected: (Interest?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // ── Status tabs (iOS-style: All · Going · Hosting · Saved) ────────────
        val statusEntries = FeedStatusFilter.entries
        TabRow(
            selectedTabIndex = statusEntries.indexOf(selectedStatus),
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            statusEntries.forEach { status ->
                Tab(
                    selected = status == selectedStatus,
                    onClick = { onStatusSelected(status) },
                    text = {
                        Text(
                            text = status.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (status == selectedStatus) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                )
            }
        }

        // ── Interest chips (only shown when on the All tab) ───────────────────
        if (selectedStatus == FeedStatusFilter.ALL) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = selectedInterest == null,
                        onClick = { onInterestSelected(null) },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    )
                }
                items(Interest.entries) { interest ->
                    FilterChip(
                        selected = selectedInterest == interest,
                        onClick = { onInterestSelected(if (selectedInterest == interest) null else interest) },
                        label = { Text("${interest.emoji} ${interest.displayName}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    )
                }
            }
        } else {
            // Small spacer so content doesn't jump when chips hide
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
