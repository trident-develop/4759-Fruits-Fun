package org.example.project.screens

import androidx.compose.foundation.Image
import fruitsfun.composeapp.generated.resources.Res
import fruitsfun.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import org.example.project.data.MockData
import org.example.project.data.Translations
import org.example.project.data.Recipe
import org.example.project.state.AppState
import org.example.project.theme.*
import org.example.project.theme.FruitIcon

@Composable
fun RecipeDetailsScreen(
    recipeId: Int,
    appState: AppState,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit = {},
) {
    val recipe = appState.getRecipeById(recipeId)
    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Recipe not found")
        }
        return
    }

    val isFavorite = appState.isFavorite(recipeId)
    val isUserRecipe = appState.isUserRecipe(recipeId)
    var servings by remember { mutableIntStateOf(1) }
    var showNutrition by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        HeaderSection(recipe, isFavorite, isUserRecipe, appState, onBack, onEdit = { onEdit(recipeId) })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            item { ServingsSelector(servings = servings, onServingsChange = { servings = it }) }
            if (!isUserRecipe) {
                item { NutritionSection(recipe, servings, showNutrition) { showNutrition = !showNutrition } }
            }
            item { FruitsUsedSection(recipe) }
            item { IngredientsSection(recipe, servings) }
            item { StepsSection(recipe) }
            if (isUserRecipe) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            appState.deleteUserRecipe(recipeId)
                            onBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F),
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F).copy(alpha = 0.5f)),
                    ) {
                        Text(
                            text = "\uD83D\uDDD1 Delete Recipe",
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    recipe: Recipe,
    isFavorite: Boolean,
    isUserRecipe: Boolean,
    appState: AppState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(recipe.color, recipe.color.copy(alpha = 0.3f)),
                ),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Image(painter = painterResource(Res.drawable.ic_back), contentDescription = "Back", modifier = Modifier.size(24.dp))
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                        .clickable { appState.toggleFavorite(recipe.id) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (isFavorite) "❤️" else "🤍",
                        fontSize = 20.sp,
                    )
                }
                if (isUserRecipe) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                            .clickable(onClick = onEdit),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("✏️", fontSize = 18.sp)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FruitIcon(name = recipe.fruitsUsed.first(), fallbackEmoji = recipe.emoji, size = 90.dp, emojiFontSize = 72.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
            ) {
                Text(
                    text = "⏱ ${recipe.prepTime}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
private fun FruitsUsedSection(recipe: Recipe) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = Strings.get("fruits_used", LocalLanguage.current),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(recipe.fruitsUsed) { fruit ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = recipe.color.copy(alpha = 0.3f)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FruitIcon(name = fruit, fallbackEmoji = "\uD83C\uDF4F", size = 24.dp, emojiFontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Translations.fruit(fruit, LocalLanguage.current),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServingsSelector(servings: Int, onServingsChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "\uD83C\uDF7D\uFE0F " + Strings.get("servings", LocalLanguage.current),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (servings > 1) SoftBerry else SoftPink.copy(alpha = 0.4f))
                    .clickable(enabled = servings > 1) { onServingsChange(servings - 1) },
                contentAlignment = Alignment.Center,
            ) {
                Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Text(
                text = "$servings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SoftBerry)
                    .clickable { onServingsChange(servings + 1) },
                contentAlignment = Alignment.Center,
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

private val numberRegex = Regex("""(\d+(?:[./]\d+)?)""")

private fun scaleIngredient(ingredient: String, servings: Int): String {
    if (servings == 1) return ingredient
    return numberRegex.replace(ingredient) { match ->
        val original = match.groupValues[1]
        when {
            "/" in original -> {
                val parts = original.split("/")
                val num = parts[0].toDoubleOrNull() ?: return@replace original
                val den = parts[1].toDoubleOrNull() ?: return@replace original
                val scaled = (num / den) * servings
                formatNumber(scaled)
            }
            else -> {
                val value = original.toDoubleOrNull() ?: return@replace original
                val scaled = value * servings
                formatNumber(scaled)
            }
        }
    }
}

private fun formatNumber(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        val rounded = (value * 100).toLong() / 100.0
        if (rounded == rounded.toLong().toDouble()) {
            rounded.toLong().toString()
        } else {
            rounded.toString()
        }
    }
}

@Composable
private fun IngredientsSection(recipe: Recipe, servings: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "\uD83D\uDED2 " + Strings.get("ingredients", LocalLanguage.current),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                if (servings > 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "×$servings",
                        style = MaterialTheme.typography.titleMedium,
                        color = SoftBerry,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            recipe.ingredients.forEach { ingredient ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SoftBerry),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = scaleIngredient(ingredient, servings),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun StepsSection(recipe: Recipe) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = "\uD83D\uDC68\u200D\uD83C\uDF73 " + Strings.get("preparation", LocalLanguage.current),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(12.dp))
        recipe.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SoftGreen),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

// ── Nutrition ──────────────────────────────────────────────────────────

private data class NutritionInfo(
    val calories: Int,
    val protein: Double,
    val sugar: Double,
    val fiber: Double,
)

private fun estimateNutrition(recipe: Recipe, servings: Int): NutritionInfo {
    var cal = 0; var protein = 0.0; var sugar = 0.0; var fiber = 0.0

    for (ingredient in recipe.ingredients) {
        val lower = ingredient.lowercase()
        val qty = numberRegex.find(lower)?.let { m ->
            val v = m.groupValues[1]
            if ("/" in v) {
                val p = v.split("/")
                (p[0].toDoubleOrNull() ?: 1.0) / (p[1].toDoubleOrNull() ?: 1.0)
            } else v.toDoubleOrNull() ?: 1.0
        } ?: 1.0

        when {
            "mango" in lower        -> { cal += (70 * qty).toInt(); sugar += 14 * qty; fiber += 1.8 * qty; protein += 0.8 * qty }
            "banana" in lower       -> { cal += (105 * qty).toInt(); sugar += 14 * qty; fiber += 3.0 * qty; protein += 1.3 * qty }
            "strawberr" in lower    -> { cal += (50 * qty).toInt(); sugar += 7 * qty; fiber += 3.0 * qty; protein += 1.0 * qty }
            "blueberr" in lower     -> { cal += (40 * qty).toInt(); sugar += 7 * qty; fiber += 1.8 * qty; protein += 0.5 * qty }
            "raspberr" in lower     -> { cal += (35 * qty).toInt(); sugar += 3 * qty; fiber += 4.0 * qty; protein += 0.8 * qty }
            "pineapple" in lower    -> { cal += (80 * qty).toInt(); sugar += 16 * qty; fiber += 2.3 * qty; protein += 0.9 * qty }
            "kiwi" in lower         -> { cal += (45 * qty).toInt(); sugar += 7 * qty; fiber += 2.0 * qty; protein += 0.8 * qty }
            "apple" in lower        -> { cal += (95 * qty).toInt(); sugar += 19 * qty; fiber += 4.4 * qty; protein += 0.5 * qty }
            "peach" in lower        -> { cal += (60 * qty).toInt(); sugar += 13 * qty; fiber += 2.3 * qty; protein += 1.4 * qty }
            "cherry" in lower || "cherries" in lower -> { cal += (50 * qty).toInt(); sugar += 8 * qty; fiber += 1.6 * qty; protein += 1.0 * qty }
            "watermelon" in lower   -> { cal += (45 * qty).toInt(); sugar += 9 * qty; fiber += 0.6 * qty; protein += 0.9 * qty }
            "grape" in lower        -> { cal += (65 * qty).toInt(); sugar += 15 * qty; fiber += 0.8 * qty; protein += 0.6 * qty }
            "plum" in lower         -> { cal += (30 * qty).toInt(); sugar += 7 * qty; fiber += 1.0 * qty; protein += 0.5 * qty }
            "lemon" in lower || "lime" in lower -> { cal += (20 * qty).toInt(); sugar += 2 * qty; fiber += 1.6 * qty; protein += 0.6 * qty }
            "pear" in lower         -> { cal += (100 * qty).toInt(); sugar += 17 * qty; fiber += 5.5 * qty; protein += 0.6 * qty }
            "orange" in lower       -> { cal += (85 * qty).toInt(); sugar += 12 * qty; fiber += 3.0 * qty; protein += 1.2 * qty }
            "coconut milk" in lower || "coconut cream" in lower -> { cal += (150 * qty).toInt(); sugar += 2 * qty; fiber += 0.0; protein += 1.5 * qty }
            "yogurt" in lower       -> { cal += (120 * qty).toInt(); sugar += 9 * qty; fiber += 0.0; protein += 8.0 * qty }
            "milk" in lower || "almond milk" in lower -> { cal += (90 * qty).toInt(); sugar += 6 * qty; fiber += 0.0; protein += 3.0 * qty }
            "ice cream" in lower    -> { cal += (200 * qty).toInt(); sugar += 21 * qty; fiber += 0.0; protein += 3.5 * qty }
            "honey" in lower || "maple syrup" in lower || "agave" in lower -> { cal += (60 * qty).toInt(); sugar += 17 * qty }
            "oats" in lower         -> { cal += (55 * qty).toInt(); sugar += 1 * qty; fiber += 1.5 * qty; protein += 2.0 * qty }
            "spinach" in lower      -> { cal += (7 * qty).toInt(); fiber += 0.7 * qty; protein += 0.9 * qty }
            "chia" in lower || "flax" in lower -> { cal += (35 * qty).toInt(); fiber += 3.0 * qty; protein += 1.5 * qty }
            "ginger" in lower || "turmeric" in lower || "cinnamon" in lower || "vanilla" in lower || "nutmeg" in lower -> { cal += 5 }
            // ice, water, pinch — 0 cal
        }
    }

    return NutritionInfo(
        calories = cal * servings,
        protein = (protein * servings * 10.0).roundToInt() / 10.0,
        sugar = (sugar * servings * 10.0).roundToInt() / 10.0,
        fiber = (fiber * servings * 10.0).roundToInt() / 10.0,
    )
}

@Composable
private fun NutritionSection(
    recipe: Recipe,
    servings: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
    ) {
        Button(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MutedOrange.copy(alpha = 0.85f),
                contentColor = Color.White,
            ),
        ) {
            Text(
                text = if (expanded) "\uD83D\uDD25 Hide Nutrition" else "\uD83D\uDD25 Calculate Calories",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            val info = remember(recipe, servings) { estimateNutrition(recipe, servings) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Nutrition per serving" + if (servings > 1) " (×$servings)" else "",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        NutritionItem(emoji = "\uD83D\uDD25", label = "Calories", value = "${info.calories} kcal", color = MutedOrange)
                        NutritionItem(emoji = "\uD83E\uDD69", label = "Protein", value = "${info.protein}g", color = SoftBerry)
                        NutritionItem(emoji = "\uD83C\uDF6C", label = "Sugar", value = "${info.sugar}g", color = SoftPink)
                        NutritionItem(emoji = "\uD83C\uDF3F", label = "Fiber", value = "${info.fiber}g", color = SoftGreen)
                    }
                }
            }
        }
    }
}

@Composable
private fun NutritionItem(emoji: String, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(emoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
