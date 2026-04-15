@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.project.screens

import androidx.compose.foundation.Image
import fruitsfun.composeapp.generated.resources.Res
import fruitsfun.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.MockData
import org.example.project.data.Recipe
import org.example.project.state.AppState
import org.example.project.theme.*

const val USER_RECIPE_EMOJI = "\uD83D\uDC68\u200D\uD83C\uDF73"

@Composable
fun AddRecipeScreen(
    appState: AppState,
    onBack: () -> Unit,
    editRecipeId: Int? = null,
) {
    val editRecipe = editRecipeId?.let { appState.getRecipeById(it) }
    val editCategoryId = editRecipeId?.let { appState.getUserRecipeCategoryId(it) }
    val isEdit = editRecipe != null

    var title by remember { mutableStateOf(editRecipe?.title ?: "") }
    var fruitsUsed by remember { mutableStateOf(editRecipe?.fruitsUsed?.joinToString(", ") ?: "") }
    var ingredients by remember { mutableStateOf(editRecipe?.ingredients?.joinToString("\n") ?: "") }
    var steps by remember { mutableStateOf(editRecipe?.steps?.joinToString("\n") ?: "") }
    var prepTime by remember { mutableStateOf(editRecipe?.prepTime ?: "") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember {
        val initial = if (editCategoryId != null) {
            val cat = MockData.categories.find { it.id == editCategoryId }
            cat?.let { it.id to it.name }
        } else null
        mutableStateOf(initial)
    }

    val isValid = title.isNotBlank() && selectedCategory != null &&
        fruitsUsed.isNotBlank() && ingredients.isNotBlank() && steps.isNotBlank() && prepTime.isNotBlank()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            ) { focusManager.clearFocus() },
    ) {
        // Fixed header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 20.dp, top = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SoftPink.copy(alpha = 0.5f))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Image(painter = painterResource(Res.drawable.ic_back), contentDescription = "Back", modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isEdit) "$USER_RECIPE_EMOJI Edit Recipe" else "$USER_RECIPE_EMOJI Create Recipe",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text("Category", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = selectedCategory?.second ?: "Select category...",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                        ) {
                            MockData.categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text("${cat.emoji} ${cat.name}") },
                                    onClick = {
                                        selectedCategory = cat.id to cat.name
                                        categoryExpanded = false
                                    },
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Recipe title", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. My Berry Smoothie") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = fieldColors(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Fruits used (comma-separated)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fruitsUsed,
                        onValueChange = { fruitsUsed = it },
                        placeholder = { Text("e.g. Banana, Strawberry, Kiwi") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = fieldColors(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Ingredients (one per line)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        placeholder = { Text("1 banana\n1 cup milk\n...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Steps (one per line)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = steps,
                        onValueChange = { steps = it },
                        placeholder = { Text("Peel the banana.\nBlend all ingredients.\n...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Preparation time", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = prepTime,
                        onValueChange = { prepTime = it },
                        placeholder = { Text("e.g. 15 min") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = fieldColors(),
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val catId = selectedCategory!!.first
                    val fruitsList = fruitsUsed.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val ingredientsList = ingredients.lines().map { it.trim() }.filter { it.isNotBlank() }
                    val stepsList = steps.lines().map { it.trim() }.filter { it.isNotBlank() }

                    val recipe = Recipe(
                        id = editRecipeId ?: 0,
                        title = title.trim(),
                        emoji = USER_RECIPE_EMOJI,
                        fruitsUsed = fruitsList,
                        ingredients = ingredientsList,
                        steps = stepsList,
                        prepTime = prepTime.trim(),
                        color = Color(0xFFFFE5D0),
                    )
                    if (isEdit && editRecipeId != null) {
                        appState.updateUserRecipe(editRecipeId, catId, recipe)
                    } else {
                        appState.addUserRecipe(catId, recipe)
                    }
                    onBack()
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftBerry,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
                Text(
                    text = if (isEdit) Strings.get("save_changes", LocalLanguage.current) else Strings.get("add_recipe", LocalLanguage.current),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SoftBerry,
    unfocusedBorderColor = SoftPink,
    focusedContainerColor = Cream.copy(alpha = 0.3f),
)
