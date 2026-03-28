package com.vibecut.ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vibecut.ai.ui.screens.home.HomeScreen
import com.vibecut.ai.ui.screens.editor.VideoEditorScreen

@Composable
fun VibeCutNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onEditVideo = { videoUri ->
                    navController.navigate("editor/$videoUri")
                },
                onUpgrade = { /* Navigate to subscription screen */ },
                onSettings = { /* Navigate to settings */ }
            )
        }
        composable(
            route = "editor/{videoUri}",
            arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoUri = backStackEntry.arguments?.getString("videoUri") ?: ""
            VideoEditorScreen(
                videoUri = videoUri,
                onBack = { navController.popBackStack() },
                onUpgrade = { /* Navigate to subscription screen */ }
            )
        }
    }
}
