package org.example.project.data

import androidx.compose.ui.graphics.Color

data class RecipeCategory(
    val id: Int,
    val name: String,
    val emoji: String,
    val color: Color,
    val recipes: List<Recipe>,
)

data class Recipe(
    val id: Int,
    val title: String,
    val emoji: String,
    val fruitsUsed: List<String>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val color: Color,
)

data class Country(
    val name: String,
    val flag: String,
    val color: Color,
    val fruits: List<CountryFruit>,
)

data class CountryFruit(
    val name: String,
    val emoji: String,
    val description: String,
    val seasonMonths: List<Int> = listOf(1,2,3,4,5,6,7,8,9,10,11,12),
)

data class FruitInfo(
    val name: String,
    val emoji: String,
    val family: String,
    val origin: String,
    val topProducers: List<String>,
    val growingClimate: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fiber: Double,
    val sugar: Double,
    val fat: Double,
    val vitaminC: Double,
    val vitaminA: Double,
    val potassium: Int,
    val calcium: Int,
    val iron: Double,
    val water: Double,
    val funFact: String,
)

data class PlantableFruit(
    val name: String,
    val emoji: String,
    val color: Color,
)
