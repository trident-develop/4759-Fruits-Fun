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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.FruitInfo
import org.example.project.data.FruitNutritionData
import org.example.project.theme.*
import org.example.project.state.AppState
import org.example.project.theme.FruitIcon

@Composable
fun FruitDetailsScreen(
    fruitName: String,
    appState: AppState? = null,
    onBack: () -> Unit,
) {
    val fruit = FruitNutritionData.getByName(fruitName)
    if (fruit == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No details available for \"$fruitName\"", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onBack) { Text("Go Back") }
            }
        }
        return
    }

    var showCompare by remember { mutableStateOf(false) }
    var compareExpanded by remember { mutableStateOf(false) }
    var compareFruit by remember { mutableStateOf<FruitInfo?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header (fixed)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(listOf(SoftGreenLight, Cream)),
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
                if (appState != null) {
                    val isFav = appState.isFruitFavorite(fruitName)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                            .clickable { appState.toggleFavoriteFruit(fruitName) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = if (isFav) "❤️" else "🤍", fontSize = 20.sp)
                    }
                }
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FruitIcon(name = fruit.name, fallbackEmoji = fruit.emoji, size = 80.dp, emojiFontSize = 60.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = fruit.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = fruit.family,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        // Origin & Growing
        item {
            InfoCard(title = "\uD83C\uDF0D " + Strings.get("origin_growing", LocalLanguage.current)) {
                InfoRow("Origin region", fruit.origin)
                InfoRow("Climate", fruit.growingClimate)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Top producers", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                fruit.topProducers.forEachIndexed { index, country ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SoftBerry,
                            modifier = Modifier.width(24.dp),
                        )
                        Text(text = country, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Nutrition per 100g
        item {
            InfoCard(title = "\uD83E\uDD57 Nutrition per 100g") {
                NutritionBar("Calories", "${fruit.calories} kcal", fruit.calories / 400f, SoftBerry)
                NutritionBar("Water", "${fruit.water}%", (fruit.water / 100f).toFloat(), Color(0xFF64B5F6))
                NutritionBar("Carbs", "${fruit.carbs}g", (fruit.carbs / 30f).toFloat(), MutedOrange)
                NutritionBar("Sugar", "${fruit.sugar}g", (fruit.sugar / 20f).toFloat(), WarmYellow)
                NutritionBar("Fiber", "${fruit.fiber}g", (fruit.fiber / 10f).toFloat(), SoftGreen)
                NutritionBar("Protein", "${fruit.protein}g", (fruit.protein / 5f).toFloat(), SoftLavender)
                NutritionBar("Fat", "${fruit.fat}g", (fruit.fat / 35f).toFloat(), Peach)
            }
        }

        // Vitamins & Minerals
        item {
            InfoCard(title = "\uD83D\uDC8A Vitamins & Minerals") {
                NutritionBar("Vitamin C", "${fruit.vitaminC} mg", (fruit.vitaminC / 230f).toFloat(), Color(0xFFFFB74D))
                NutritionBar("Vitamin A", "${fruit.vitaminA} µg", (fruit.vitaminA / 170f).toFloat(), Color(0xFFFF8A65))
                NutritionBar("Potassium", "${fruit.potassium} mg", fruit.potassium / 500f, Color(0xFF81C784))
                NutritionBar("Calcium", "${fruit.calcium} mg", fruit.calcium / 50f, Color(0xFF4FC3F7))
                NutritionBar("Iron", "${fruit.iron} mg", (fruit.iron / 3f).toFloat(), Color(0xFFE57373))
            }
        }

        // Fun fact
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmYellow.copy(alpha = 0.3f)),
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("💡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = fruit.funFact,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        // Compare button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showCompare = !showCompare },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showCompare) MaterialTheme.colorScheme.onSurfaceVariant else SoftBerry,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = if (showCompare) "Hide Comparison" else "\uD83D\uDD0D Compare with another fruit",
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // Compare section
        if (showCompare) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = compareExpanded,
                    onExpandedChange = { compareExpanded = it },
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    OutlinedTextField(
                        value = compareFruit?.name ?: "Select a fruit to compare...",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = compareExpanded) },
                    )
                    ExposedDropdownMenu(
                        expanded = compareExpanded,
                        onDismissRequest = { compareExpanded = false },
                    ) {
                        FruitNutritionData.allFruits
                            .filter { it.name != fruit.name }
                            .forEach { f ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            FruitIcon(name = f.name, fallbackEmoji = f.emoji, size = 24.dp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(f.name)
                                        }
                                    },
                                    onClick = {
                                        compareFruit = f
                                        compareExpanded = false
                                    },
                                )
                            }
                    }
                }
            }

            if (compareFruit != null) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    ComparisonTable(fruit, compareFruit!!)
                }
            }
        }
    }
    }
}

@Composable
private fun ComparisonTable(a: FruitInfo, b: FruitInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("", modifier = Modifier.weight(1.2f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FruitIcon(name = a.name, fallbackEmoji = a.emoji, size = 32.dp)
                    Text(a.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FruitIcon(name = b.name, fallbackEmoji = b.emoji, size = 32.dp)
                    Text(b.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = SoftPink.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(4.dp))
            CompareRow("Calories", "${a.calories} kcal", "${b.calories} kcal", a.calories < b.calories)
            CompareRow("Carbs", "${a.carbs}g", "${b.carbs}g", a.carbs < b.carbs)
            CompareRow("Sugar", "${a.sugar}g", "${b.sugar}g", a.sugar < b.sugar)
            CompareRow("Fiber", "${a.fiber}g", "${b.fiber}g", a.fiber > b.fiber)
            CompareRow("Protein", "${a.protein}g", "${b.protein}g", a.protein > b.protein)
            CompareRow("Fat", "${a.fat}g", "${b.fat}g", a.fat < b.fat)
            CompareRow("Vitamin C", "${a.vitaminC} mg", "${b.vitaminC} mg", a.vitaminC > b.vitaminC)
            CompareRow("Potassium", "${a.potassium} mg", "${b.potassium} mg", a.potassium > b.potassium)
            CompareRow("Calcium", "${a.calcium} mg", "${b.calcium} mg", a.calcium > b.calcium)
            CompareRow("Iron", "${a.iron} mg", "${b.iron} mg", a.iron > b.iron)
            CompareRow("Water", "${a.water}%", "${b.water}%", a.water > b.water)
        }
    }
}

@Composable
private fun CompareRow(label: String, valA: String, valB: String, aIsBetter: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.2f),
        )
        Text(
            text = valA,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (aIsBetter) FontWeight.Bold else FontWeight.Normal,
            color = if (aIsBetter) SoftGreen else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Text(
            text = valB,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (!aIsBetter) FontWeight.Bold else FontWeight.Normal,
            color = if (!aIsBetter) SoftGreen else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.5f),
        )
    }
}

@Composable
private fun NutritionBar(label: String, value: String, fraction: Float, color: Color) {
    val clampedFraction = fraction.coerceIn(0f, 1f)
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.15f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(clampedFraction)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color),
            )
        }
    }
}
