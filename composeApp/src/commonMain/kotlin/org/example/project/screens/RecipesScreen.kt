package org.example.project.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.MockData
import org.example.project.data.Translations
import org.example.project.data.Recipe
import org.example.project.data.RecipeCategory
import org.example.project.state.AppState
import org.example.project.theme.*
import org.example.project.theme.FruitIcon

@Composable
fun RecipesScreen(appState: AppState, onRecipeClick: (Int) -> Unit, onAddRecipeClick: () -> Unit = {}) {
    val expandedCategories = remember { mutableStateMapOf<Int, Boolean>() }
    var searchQuery by remember { mutableStateOf("") }
    val categories = appState.allCategories

    val filteredCategories = if (searchQuery.isBlank()) {
        categories
    } else {
        val query = searchQuery.trim().lowercase()
        categories.mapNotNull { category ->
            val matchingRecipes = category.recipes.filter { recipe ->
                recipe.fruitsUsed.any { it.lowercase().contains(query) }
            }
            if (matchingRecipes.isNotEmpty()) {
                category.copy(recipes = matchingRecipes)
            } else null
        }
    }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize().clickable(
        indication = null,
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
    ) { focusManager.clearFocus() }) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "\uD83E\uDD64 " + Strings.get("recipes", LocalLanguage.current),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp),
        )
        Text(
            text = Strings.get("explore_recipes", LocalLanguage.current),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(Strings.get("search_fruit", LocalLanguage.current)) },
            leadingIcon = { Text("\uD83D\uDD0D", fontSize = 18.sp) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "✕",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { searchQuery = "" },
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SoftBerry,
                unfocusedBorderColor = SoftPink,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (filteredCategories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No recipes found with \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filteredCategories, key = { it.id }) { category ->
                    val isExpanded = expandedCategories[category.id] ?: false
                    CategoryCard(
                        category = category,
                        isExpanded = isExpanded,
                        onToggle = { expandedCategories[category.id] = !isExpanded },
                        onRecipeClick = onRecipeClick,
                        appState = appState,
                    )
                }
                item { Spacer(modifier = Modifier.height(72.dp)) }
            }
        }
    }

    FloatingActionButton(
        onClick = onAddRecipeClick,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 20.dp, bottom = 16.dp),
        shape = RoundedCornerShape(18.dp),
        containerColor = SoftBerry,
        contentColor = Color.White,
    ) {
        Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
    }
}

@Composable
private fun CategoryCard(
    category: RecipeCategory,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    appState: AppState,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = category.color.copy(alpha = 0.3f),
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(category.color),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = category.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Translations.category(category.name, LocalLanguage.current),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${category.recipes.size} recipes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = if (isExpanded) "▲" else "▼",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HorizontalDivider(color = category.color.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(4.dp))
                    category.recipes.forEach { recipe ->
                        RecipeItem(
                            recipe = recipe,
                            isFavorite = appState.isFavorite(recipe.id),
                            onClick = { onRecipeClick(recipe.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeItem(recipe: Recipe, isFavorite: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(recipe.color.copy(alpha = 0.35f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FruitIcon(name = recipe.fruitsUsed.first(), fallbackEmoji = recipe.emoji, size = 34.dp, emojiFontSize = 28.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "⏱ ${recipe.prepTime}  •  ${recipe.fruitsUsed.size} fruits",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (isFavorite) {
            Text(text = "❤️", fontSize = 16.sp, modifier = Modifier.padding(end = 6.dp))
        }
        Text(text = "›", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
