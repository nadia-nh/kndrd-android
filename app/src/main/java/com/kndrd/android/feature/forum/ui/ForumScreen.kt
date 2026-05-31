package com.kndrd.android.feature.forum.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kndrd.android.core.model.ForumPost
import com.kndrd.android.core.model.Interest
import com.kndrd.android.feature.forum.ForumViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(viewModel: ForumViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Community", fontWeight = FontWeight.Bold) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "New post")
            }
        }
    ) { innerPadding ->
        if (state.posts.isEmpty() && !state.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No posts yet — start the conversation!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.posts, key = { it.id }) { post ->
                    ForumPostCard(post = post, onUpvote = { viewModel.upvotePost(post.id) })
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            CreatePostBottomSheet(
                onPost = { title, body, interest ->
                    viewModel.createPost(title, body, interest) { showSheet = false }
                },
                onDismiss = { showSheet = false },
            )
        }
    }
}

@Composable
fun ForumPostCard(post: ForumPost, onUpvote: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(post.authorName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(
                    formatRelativeTime(post.timestampMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            post.interest?.let {
                SuggestionChip(onClick = {}, label = { Text("${it.emoji} ${it.displayName}") })
            }
            Text(post.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(
                post.body,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onUpvote) {
                        Icon(
                            Icons.Outlined.ThumbUp,
                            contentDescription = "Upvote",
                            tint = if (post.isUpvotedByCurrentUser) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text("${post.upvotes}", style = MaterialTheme.typography.labelSmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(" ${post.replyCount}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreatePostBottomSheet(
    onPost: (title: String, body: String, interest: Interest?) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var interest by remember { mutableStateOf<Interest?>(null) }
    var interestExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("New Post", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("What's on your mind?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
        )
        ExposedDropdownMenuBox(
            expanded = interestExpanded,
            onExpandedChange = { interestExpanded = it },
        ) {
            OutlinedTextField(
                value = interest?.let { "${it.emoji} ${it.displayName}" } ?: "No tag",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tag (optional)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = interestExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
            )
            ExposedDropdownMenu(
                expanded = interestExpanded,
                onDismissRequest = { interestExpanded = false },
            ) {
                DropdownMenuItem(text = { Text("No tag") }, onClick = { interest = null; interestExpanded = false })
                Interest.entries.forEach { i ->
                    DropdownMenuItem(
                        text = { Text("${i.emoji} ${i.displayName}") },
                        onClick = { interest = i; interestExpanded = false },
                    )
                }
            }
        }
        Button(
            onClick = { onPost(title, body, interest) },
            enabled = title.isNotBlank() && body.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Post")
        }
    }
}

private fun formatRelativeTime(ms: Long): String {
    val diff = System.currentTimeMillis() - ms
    return when {
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else -> SimpleDateFormat("MMM d", Locale.US).format(Date(ms))
    }
}
