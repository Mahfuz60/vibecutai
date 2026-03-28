package com.vibecut.ai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vibecut.ai.domain.repository.Project

@Composable
fun AiCreditsChip(credits: Int, onUpgrade: () -> Unit) {
    AssistChip(
        onClick = onUpgrade,
        label = { Text("$credits AI Credits") }
    )
}

@Composable
fun AiFeaturesBanner(onUpgrade: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        onClick = onUpgrade
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Unlock AI Magic", style = MaterialTheme.typography.titleMedium)
            Text("Auto-beat sync, background removal & more.")
        }
    }
}

@Composable
fun EmptyProjectsState(onPickVideo: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onPickVideo) {
            Text("Start Your First Project")
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().height(200.dp), onClick = onClick) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Text(project.name, modifier = Modifier.padding(8.dp))
        }
    }
}
