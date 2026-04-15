package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.example.project.screens.AddRecipeScreen
import org.example.project.screens.FruitDetailsScreen
import org.example.project.screens.LoadingScreen
import org.example.project.screens.MainScreen
import org.example.project.screens.OnboardingScreen
import org.example.project.screens.RecipeDetailsScreen
import org.example.project.screens.SettingsScreen
import org.example.project.screens.WebViewScreen
import org.example.project.state.AppState
import org.example.project.theme.FruitTheme

@Serializable data object LoadingRoute
@Serializable data object OnboardingRoute
@Serializable data object MainRoute
@Serializable data class RecipeDetailsRoute(val recipeId: Int)
@Serializable data class AddRecipeRoute(val editRecipeId: Int = -1)
@Serializable data class FruitDetailsRoute(val fruitName: String)
@Serializable data object SettingsRoute
@Serializable data class WebViewRoute(val url: String, val title: String)

@Composable
fun App() {
    val preferences = remember { createAppPreferences() }
    val appState = remember { AppState(preferences) }
    val navController = rememberNavController()

    FruitTheme(theme = appState.appTheme.value, language = appState.appLanguage.value) {
        NavHost(
            navController = navController,
            startDestination = LoadingRoute,
        ) {
            composable<LoadingRoute> {
                LoadingScreen(
                    onLoadingComplete = {
                        if (preferences.isOnboardingCompleted()) {
                            navController.navigate(MainRoute) {
                                popUpTo<LoadingRoute> { inclusive = true }
                            }
                        } else {
                            navController.navigate(OnboardingRoute) {
                                popUpTo<LoadingRoute> { inclusive = true }
                            }
                        }
                    },
                )
            }
            composable<OnboardingRoute> {
                OnboardingScreen(
                    onFinished = {
                        preferences.setOnboardingCompleted()
                        navController.navigate(MainRoute) {
                            popUpTo<OnboardingRoute> { inclusive = true }
                        }
                    },
                )
            }
            composable<MainRoute> {
                MainScreen(
                    appState = appState,
                    onRecipeClick = { recipeId ->
                        navController.navigate(RecipeDetailsRoute(recipeId))
                    },
                    onAddRecipeClick = {
                        navController.navigate(AddRecipeRoute())
                    },
                    onEditRecipeClick = { recipeId ->
                        navController.navigate(AddRecipeRoute(editRecipeId = recipeId))
                    },
                    onFruitClick = { fruitName ->
                        navController.navigate(FruitDetailsRoute(fruitName))
                    },
                    onSettingsClick = {
                        navController.navigate(SettingsRoute)
                    },
                    onWebViewClick = { url, title ->
                        navController.navigate(WebViewRoute(url, title))
                    },
                )
            }
            composable<AddRecipeRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<AddRecipeRoute>()
                val editId = route.editRecipeId.takeIf { it >= 0 }
                AddRecipeScreen(
                    appState = appState,
                    onBack = { navController.popBackStack() },
                    editRecipeId = editId,
                )
            }
            composable<WebViewRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<WebViewRoute>()
                WebViewScreen(
                    url = route.url,
                    title = route.title,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<SettingsRoute> {
                SettingsScreen(
                    appState = appState,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<FruitDetailsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<FruitDetailsRoute>()
                FruitDetailsScreen(
                    fruitName = route.fruitName,
                    appState = appState,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<RecipeDetailsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<RecipeDetailsRoute>()
                RecipeDetailsScreen(
                    recipeId = route.recipeId,
                    appState = appState,
                    onBack = { navController.popBackStack() },
                    onEdit = { recipeId ->
                        navController.navigate(AddRecipeRoute(editRecipeId = recipeId))
                    },
                )
            }
        }
    }
}
