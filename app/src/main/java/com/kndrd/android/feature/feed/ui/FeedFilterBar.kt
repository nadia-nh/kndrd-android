package com.kndrd.android.feature.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.kndrd.android.core.model.Interest

@Composable
fun FeedFilterBar(
    selectedInterest: Interest?,
    onInterestSelected: (Interest?) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            FilterChip(
                selected = selectedInterest == null,
                onClick = { onInterestSelected(null) },
                label = { Text("All") },
            )
        }
        items(Interest.entries) { interest ->
            FilterChip(
                selected = selectedInterest == interest,
                onClick = { onInterestSelected(if (selectedInterest == interest) null else interest) },
                label = { Text("${interest.emoji} ${interest.displayName}") },
            )
        }
    }
}
