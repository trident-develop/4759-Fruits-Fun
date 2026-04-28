package org.example.project

import kotlinx.serialization.Serializable

@Serializable data object LoadingRoute
@Serializable data object OnboardingRoute
@Serializable data object MainRoute
@Serializable data class RecipeDetailsRoute(val recipeId: Int)
@Serializable data class AddRecipeRoute(val editRecipeId: Int = -1)
@Serializable data class FruitDetailsRoute(val fruitName: String)
@Serializable data object SettingsRoute
@Serializable data class WebViewRoute(val url: String, val title: String)

@Serializable data object ConnectRoute