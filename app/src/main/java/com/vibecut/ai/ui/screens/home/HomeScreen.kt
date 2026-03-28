package com.vibecut.ai.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vibecut.ai.ui.theme.VibeCutColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEditVideo: (String) -> Unit,
    onUpgrade: () -> Unit,
    onSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onEditVideo(Uri.encode(it.toString())) }
    }

    Scaffold(
        containerColor = VibeCutColors.Background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VibeCut AI",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Professional Video Editor",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.Gray,
                        letterSpacing = 2.sp
                    )
                )
            }
        },
        bottomBar = {
            Surface(
                color = Color(0xFF121212),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ToolIcon(Icons.Default.Crop, "CANVAS")
                        ToolIcon(Icons.Default.ContentCut, "TRIM")
                        ToolIcon(Icons.Default.Filter, "FILTER")
                        Spacer(modifier = Modifier.width(48.dp)) // Space for FAB
                        ToolIcon(Icons.Default.MusicNote, "MUSIC")
                        ToolIcon(Icons.Default.Face, "STICKER")
                        ToolIcon(Icons.Default.Title, "TEXT")
                    }
                    
                    FloatingActionButton(
                        onClick = { videoPickerLauncher.launch("video/*") },
                        containerColor = Color(0xFFFF4081),
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-24).dp)
                            .size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Project", modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.projects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF1E1E1E)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No Projects Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.LightGray
                        )
                        Text(
                            "Tap + to start your first creation",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.projects) { project ->
                        ProjectItem(
                            project = project,
                            onClick = { onEditVideo(project.videoUri) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectItem(project: com.vibecut.ai.domain.repository.Project, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1E1E))
        ) {
            AsyncImage(
                model = project.videoUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = project.name,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun ToolIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 8.sp)
    }
}
