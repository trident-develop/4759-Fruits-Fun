package org.example.project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.model.ScoreSource
import org.example.project.nav.RouteBus
import org.example.project.nav.isGame
import org.example.project.nav.isLoading
import org.example.project.nav.isRules
import org.example.project.nav.rememberRouteToken
import org.example.project.screens.AddRecipeScreen
import org.example.project.screens.ConnectScreen
import org.example.project.screens.FruitDetailsScreen
import org.example.project.screens.LoadingScreen
import org.example.project.screens.MainScreen
import org.example.project.screens.OnboardingScreen
import org.example.project.screens.RecipeDetailsScreen
import org.example.project.screens.SettingsScreen
import org.example.project.screens.WebViewScreen
import org.example.project.screens.isFlowersConnected
import org.example.project.screens.privacy.Show3
import org.example.project.state.AppState
import org.example.project.theme.FruitTheme
import org.example.project.utils.ShiftCodec
import org.example.project.utils.ShiftCodec.DM
import org.example.project.utils.log
import org.example.project.viewmodel.LoadingViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun App(show3: Show3) {
    val preferences = remember { createAppPreferences() }
    val appState = remember { AppState(preferences) }
    val navController = rememberNavController()
    val context = LocalContext.current as MainActivity

    FruitTheme(theme = appState.appTheme.value, language = appState.appLanguage.value) {
        NavHost(
            navController = navController,
            startDestination = if (context.isFlowersConnected()) LoadingRoute else ConnectRoute,
        ) {
            composable<LoadingRoute> {

                val viewModel: LoadingViewModel = koinViewModel()
                val scoreState = viewModel.scoreState.collectAsState()
                val route = rememberRouteToken()

                LaunchedEffect(Unit) { viewModel.loadScore() }

                LaunchedEffect(scoreState.value) {
                    val result = scoreState.value
//                    log("result = $result")

                    val score = result?.score
                    val source = result?.source

//                    log("score = $score, source = $source")
//                    log("score $score")
                    if (!score.isNullOrBlank()) {
                        if (source == ScoreSource.BUILT){
                            show3.loadUrl(score)
                        } else {
                            if (!score.startsWith("${ShiftCodec.decode(DM)}/")) {
                                show3.loadUrl(score)
                            } else {
                                RouteBus.game()
                            }
                        }
                    }
                }

                when {
                    route.isLoading() -> LoadingScreen({})
                    route.isGame() -> {
                        LaunchedEffect(Unit) {
                            if (preferences.isOnboardingCompleted()) {
                                navController.navigate(MainRoute) {
                                    popUpTo<LoadingRoute> { inclusive = true }
                                }
                            } else {
                                navController.navigate(OnboardingRoute) {
                                    popUpTo<LoadingRoute> { inclusive = true }
                                }
                            }
                        }
                    }
                    route.isRules() -> { OnboardingScreen({}) }
                }

                LoadingScreen({})
            }
            composable<ConnectRoute> { ConnectScreen(navController) }
            composable<OnboardingRoute> {
                OnboardingScreen(
                    onFinished = {
                        preferences.setOnboardingCompleted()
                        navController.navigate(MainRoute) {
                            popUpTo<OnboardingRoute> { inclusive = true }
                        }
                    },
                    onExit = { context.finish() },
                )
            }
            composable<MainRoute> {
                BackHandler {
                    if (appState.selectedTabIndex.value != 0) {
                        appState.selectedTabIndex.value = 0
                    } else {
                        context.finish()
                    }
                }
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