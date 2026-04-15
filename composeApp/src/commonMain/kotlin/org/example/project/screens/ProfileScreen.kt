package org.example.project.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import org.example.project.isIOS
import org.example.project.state.AppState
import org.example.project.data.Translations
import org.example.project.theme.*
import org.example.project.theme.FruitIcon

@Composable
fun ProfileScreen(appState: AppState, onWebViewClick: (String, String) -> Unit = { _, _ -> }, onRecipeClick: (Int) -> Unit = {}, onFruitClick: (String) -> Unit = {}) {
    val favoriteRecipes = appState.allRecipes.filter { it.id in appState.favoriteRecipeIds }
    val gardenGrouped = appState.plantedFruitsGrouped
    var gardenExpanded by remember { mutableStateOf(false) }
    var fruitsExpanded by remember { mutableStateOf(false) }
    var recipesExpanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxSize()
        .clickable(
            indication = null,
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        ) { focusManager.clearFocus() },
    ) {
    Text(
        text = "\uD83D\uDC64 " + Strings.get("profile", LocalLanguage.current),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp),
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {

        // User card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var showPhotoDialog by remember { mutableStateOf(false) }
                    val imagePickerLaunchers = rememberImagePickerLaunchers { bytes ->
                        appState.setProfileImage(bytes)
                    }

                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            ) { showPhotoDialog = true },
                        contentAlignment = Alignment.Center,
                    ) {
                        // Avatar circle
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Peach, SoftPink),
                                    ),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            val imageBytes = appState.profileImageBytes.value
                            if (imageBytes != null) {
                                Image(
                                    bitmap = remember(imageBytes) { bytesToImageBitmap(imageBytes) },
                                    contentDescription = "Profile photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Text(text = "\uD83D\uDC64", fontSize = 40.sp)
                            }
                        }
                        // Camera button — outside the clip
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(SoftBerry),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("\uD83D\uDCF7", fontSize = 14.sp)
                        }
                    }

                    PermissionDeniedDialog(imagePickerLaunchers)

                    if (showPhotoDialog) {
                        AlertDialog(
                            onDismissRequest = { showPhotoDialog = false },
                            title = { Text("Profile Photo") },
                            text = { Text("Choose how to set your profile photo") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showPhotoDialog = false
                                    imagePickerLaunchers.launchCamera()
                                }) { Text("\uD83D\uDCF7 Camera") }
                            },
                            dismissButton = {
                                Row {
                                    if (appState.profileImageBytes.value != null) {
                                        TextButton(onClick = {
                                            showPhotoDialog = false
                                            appState.setProfileImage(null)
                                        }) { Text("\uD83D\uDDD1 Remove", color = Color(0xFFD32F2F)) }
                                    }
                                    TextButton(onClick = {
                                        showPhotoDialog = false
                                        imagePickerLaunchers.launchGallery()
                                    }) { Text("\uD83D\uDDBC Gallery") }
                                }
                            },
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = appState.userName.value,
                        onValueChange = { if (it.length <= 20) appState.setUserName(it) },
                        label = { Text(Strings.get("your_name", LocalLanguage.current)) },
                        placeholder = { Text(Strings.get("enter_name", LocalLanguage.current)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBerry,
                            unfocusedBorderColor = SoftPink,
                            focusedLabelColor = SoftBerry,
                        ),
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Stats row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    emoji = "❤️",
                    label = "Favorites",
                    count = favoriteRecipes.size.toString(),
                    color = LightCoral,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    emoji = "\uD83C\uDF4F",
                    label = "Fruits",
                    count = appState.favoriteFruitNames.size.toString(),
                    color = MutedOrange,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    emoji = "\uD83C\uDF31",
                    label = "Planted",
                    count = "${appState.occupiedSlots}/${appState.gardenSlots.size}",
                    color = SoftGreen,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Garden Stats — collapsible
        item {
            CollapsibleSection(
                emoji = "\uD83C\uDF3F",
                title = Strings.get("garden_stats", LocalLanguage.current),
                count = gardenGrouped.size,
                isExpanded = gardenExpanded,
                onToggle = { gardenExpanded = !gardenExpanded },
            ) {
                if (gardenGrouped.isEmpty()) {
                    Text(
                        text = Strings.get("no_planted", LocalLanguage.current),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    )
                } else {
                    gardenGrouped.entries.sortedByDescending { it.value }.forEach { (fruit, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FruitIcon(name = fruit, size = 28.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = Translations.fruit(fruit, LocalLanguage.current),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(SoftGreenLight),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("×$count", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Favorite Fruits — collapsible
        item {
            CollapsibleSection(
                emoji = "\uD83C\uDF4F",
                title = "Favorite Fruits",
                count = appState.favoriteFruitNames.size,
                isExpanded = fruitsExpanded,
                onToggle = { fruitsExpanded = !fruitsExpanded },
            ) {
                if (appState.favoriteFruitNames.isEmpty()) {
                    Text(
                        text = "No favorite fruits yet.\nTap the heart on any fruit!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    )
                } else {
                    appState.favoriteFruitNames.forEach { fruitName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onFruitClick(fruitName) }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FruitIcon(name = fruitName, size = 28.dp, emojiFontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(fruitName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text("›", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Favorite Recipes — collapsible
        item {
            CollapsibleSection(
                emoji = "❤️",
                title = Strings.get("favorite_recipes", LocalLanguage.current),
                count = favoriteRecipes.size,
                isExpanded = recipesExpanded,
                onToggle = { recipesExpanded = !recipesExpanded },
            ) {
                if (favoriteRecipes.isEmpty()) {
                    Text(
                        text = Strings.get("no_favorites", LocalLanguage.current),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    )
                } else {
                    favoriteRecipes.forEach { recipe ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onRecipeClick(recipe.id) }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FruitIcon(name = recipe.fruitsUsed.first(), fallbackEmoji = recipe.emoji, size = 28.dp, emojiFontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(recipe.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text("⏱ ${recipe.prepTime}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("›", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Calorie Calculator
        item { CalorieCalculatorSection(appState) }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Legal section
        item {
            Text(
                text = "Legal",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
        }
        item {
            val privacyUrl = if (isIOS) {
                "https://telegra.ph/Berry-Garden-Land-Privacy-Policy-04-15"
            } else {
                "https://telegra.ph/Privacy-Policy-for-Fruits-Fun-04-15"
            }
            LegalRow(
                label = "\uD83D\uDD12 Privacy Policy",
                onClick = {
                    onWebViewClick(privacyUrl, "Privacy Policy")
                },
            )
        }
        if (isIOS) {
            item {
                LegalRow(
                    label = "\uD83D\uDCDC Terms of Use",
                    onClick = {
                        onWebViewClick(
                            "https://telegra.ph/Berry-Garden-Land-Terms-of-Use-04-15",
                            "Terms of Use",
                        )
                    },
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
    }
}

@Composable
private fun LegalRow(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "›",
                fontSize = 18.sp,
                color = SoftBerry,
            )
        }
    }
}

@Composable
private fun CollapsibleSection(
    emoji: String,
    title: String,
    count: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(emoji, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "$count items",
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
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    emoji: String,
    label: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CalorieCalculatorSection(appState: AppState) {
    var expanded by remember { mutableStateOf(false) }
    var heightCm by remember { mutableStateOf(appState.calcHeight.value) }
    var weightKg by remember { mutableStateOf(appState.calcWeight.value) }
    var isMale by remember { mutableStateOf(appState.calcIsMale.value) }
    var result by remember { mutableStateOf(appState.calcResult.value) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("\uD83E\uDDEE", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Calorie Calculator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Daily calorie intake",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Gender toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        GenderChip(
                            label = "\uD83D\uDE4B\u200D♂\uFE0F Male",
                            selected = isMale,
                            onClick = { isMale = true },
                            modifier = Modifier.weight(1f),
                        )
                        GenderChip(
                            label = "\uD83D\uDE4B\u200D♀\uFE0F Female",
                            selected = !isMale,
                            onClick = { isMale = false },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Height
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) heightCm = it },
                        label = { Text("Height (cm)") },
                        placeholder = { Text("170") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBerry,
                            unfocusedBorderColor = SoftPink,
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weight
                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) weightKg = it },
                        label = { Text("Weight (kg)") },
                        placeholder = { Text("65") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftBerry,
                            unfocusedBorderColor = SoftPink,
                        ),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Calculate button
                    val canCalculate = heightCm.isNotBlank() && weightKg.isNotBlank()
                    Button(
                        onClick = {
                            val h = heightCm.toDoubleOrNull() ?: return@Button
                            val w = weightKg.toDoubleOrNull() ?: return@Button
                            // Mifflin-St Jeor (age assumed 25, moderate activity x1.55)
                            val bmr = if (isMale) {
                                10 * w + 6.25 * h - 5 * 25 + 5
                            } else {
                                10 * w + 6.25 * h - 5 * 25 - 161
                            }
                            val cal = (bmr * 1.55).roundToInt()
                            result = cal
                            appState.saveCalcData(heightCm, weightKg, isMale, cal)
                        },
                        enabled = canCalculate,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftBerry,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text("Calculate", fontWeight = FontWeight.SemiBold)
                    }

                    // Result
                    if (result != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftGreenLight),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "\uD83D\uDD25 $result kcal/day",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Recommended daily intake\n(moderate activity level)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkText.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenderChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) SoftBerry.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        border = if (selected) androidx.compose.foundation.BorderStroke(1.5.dp, SoftBerry) else null,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) SoftBerry else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
