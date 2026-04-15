@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.project.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.state.AppState
import org.example.project.data.Translations
import org.example.project.state.FERTILIZER_COST
import org.example.project.state.FruitGrowTimes
import org.example.project.state.GardenSlot
import org.example.project.state.WATER_COST
import org.example.project.state.currentTimeMs
import org.example.project.theme.*
import org.example.project.theme.hasDistinctIcon

private val soilDark = Color(0xFF8B6F5E)
private val soilLight = Color(0xFFC4A882)
private val soilBed = Color(0xFFDEC9A8)
private val grassGreen = Color(0xFFB8D8A8)
private val harvestGold = Color(0xFFFFD54F)

@Composable
fun GardenScreen(appState: AppState) {
    var expanded by remember { mutableStateOf(false) }
    var selectedFruit by remember { mutableStateOf<String?>(null) }
    val allFruits = remember { appState.allAvailableFruits.filter { hasDistinctIcon(it) } }

    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val triggerShake: () -> Unit = {
        scope.launch {
            repeat(3) {
                shakeOffset.animateTo(10f, tween(50, easing = LinearEasing))
                shakeOffset.animateTo(-10f, tween(50, easing = LinearEasing))
            }
            shakeOffset.animateTo(0f, tween(50, easing = LinearEasing))
        }
    }

    // Tick every second to update progress
    var now by remember { mutableLongStateOf(currentTimeMs()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = currentTimeMs()
        }
    }

    // Toast message
    val snackbarHostState = remember { SnackbarHostState() }
    var lastToastTime by remember { mutableLongStateOf(0L) }
    val showToast: (String) -> Unit = { message ->
        val currentMs = currentTimeMs()
        if (currentMs - lastToastTime > 2000) {
            lastToastTime = currentMs
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }
    val showNotEnoughPoints: () -> Unit = { showToast("Not enough points! ⭐") }
    val showSelectFruit: () -> Unit = { showToast("Select a fruit first! \uD83C\uDF4F") }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
    Text(
        text = "\uD83C\uDF3F " + Strings.get("my_garden", LocalLanguage.current),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp),
    )
    Text(
        text = Strings.get("grow_paradise", LocalLanguage.current),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 20.dp, bottom = 16.dp),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        // Banner with score
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE8F5E9),
                                grassGreen,
                                Color(0xFF9CCC65).copy(alpha = 0.6f),
                            ),
                        ),
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "\uD83C\uDFE1\uD83C\uDF33\uD83C\uDF3B\uD83C\uDF3A\uD83C\uDF33", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Strings.get("your_garden", LocalLanguage.current),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⭐ ${appState.gardenScore.intValue}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Points",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmBrown,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(WarmBrown.copy(alpha = 0.3f)),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "\uD83C\uDF4E ${appState.harvestedCount.intValue}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Harvested",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmBrown,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(WarmBrown.copy(alpha = 0.3f)),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "\uD83C\uDF31 ${appState.occupiedSlots}/${appState.gardenSlots.size}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Beds",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmBrown,
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Fruit selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(x = shakeOffset.value.dp)
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "\uD83C\uDF31 " + Strings.get("select_fruit", LocalLanguage.current),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    if (selectedFruit != null) {
                        val mins = FruitGrowTimes.getGrowMinutes(selectedFruit!!)
                        val pts = FruitGrowTimes.getPoints(selectedFruit!!)
                        val timeStr = if (mins >= 60) "${mins / 60}h ${mins % 60}m" else "${mins}m"
                        Text(
                            text = "⏱ Grows in $timeStr  •  ⭐ $pts pts",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftBerry,
                            fontWeight = FontWeight.Medium,
                        )
                    } else {
                        Text(
                            text = Strings.get("choose_then_tap", LocalLanguage.current),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        OutlinedTextField(
                            value = selectedFruit ?: "Choose a fruit...",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftGreen,
                                unfocusedBorderColor = SoftGreenLight,
                                focusedContainerColor = SoftGreenLight.copy(alpha = 0.2f),
                            ),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            leadingIcon = if (selectedFruit != null) {
                                { FruitIcon(name = selectedFruit!!, size = 28.dp) }
                            } else null,
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            allFruits.forEach { fruitName ->
                                val mins = FruitGrowTimes.getGrowMinutes(fruitName)
                                val pts = FruitGrowTimes.getPoints(fruitName)
                                val timeStr = if (mins >= 60) "${mins / 60}h${if (mins % 60 > 0) " ${mins % 60}m" else ""}" else "${mins}m"
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            FruitIcon(name = fruitName, size = 26.dp)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(Translations.fruit(fruitName, LocalLanguage.current), style = MaterialTheme.typography.bodyMedium)
                                                Text(
                                                    "⏱ $timeStr  ⭐ $pts",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedFruit = fruitName
                                        expanded = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Booster shop
        item {
            Text(
                text = "\uD83D\uDED2 " + Strings.get("garden_shop", LocalLanguage.current),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
            Text(
                text = Strings.get("shop_hint", LocalLanguage.current),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BoosterCard(
                    emoji = "\uD83D\uDCA7",
                    name = "Water",
                    description = "Speed +20%",
                    cost = WATER_COST,
                    canAfford = appState.gardenScore.intValue >= WATER_COST,
                    color = Color(0xFF64B5F6),
                    modifier = Modifier.weight(1f),
                )
                BoosterCard(
                    emoji = "\uD83E\uDEB4",
                    name = "Fertilizer",
                    description = "Speed +40%",
                    cost = FERTILIZER_COST,
                    canAfford = appState.gardenScore.intValue >= FERTILIZER_COST,
                    color = Color(0xFF81C784),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Garden beds title
        item {
            Text(
                text = "\uD83C\uDF3E " + Strings.get("garden_beds", LocalLanguage.current),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
        }

        // Garden grid — 2 columns
        val slots = appState.gardenSlots
        val rows = slots.chunked(2)
        rows.forEachIndexed { rowIndex, row ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    row.forEachIndexed { colIndex, slot ->
                        val slotIndex = rowIndex * 2 + colIndex
                        val isUnlocked = appState.isSlotUnlocked(slotIndex)

                        if (!isUnlocked) {
                            val cost = appState.slotUnlockCost(slotIndex)
                            val canAfford = appState.gardenScore.intValue >= cost
                            LockedBed(
                                slotNumber = slotIndex + 1,
                                cost = cost,
                                canAfford = canAfford,
                                modifier = Modifier.weight(1f),
                                onUnlock = {
                                    if (!appState.unlockNextSlot()) showNotEnoughPoints()
                                },
                            )
                        } else {
                            val progress = slot?.let {
                                ((now - it.plantedAtMs).toFloat() / it.growDurationMs).coerceIn(0f, 1f)
                            } ?: 0f
                            val isReady = progress >= 1f
                            val timeRemaining = slot?.let {
                                val remaining = (it.growDurationMs - (now - it.plantedAtMs)).coerceAtLeast(0)
                                if (remaining == 0L) "Ready!"
                                else {
                                    val s = remaining / 1000
                                    val h = s / 3600; val m = (s % 3600) / 60; val sec = s % 60
                                    if (h > 0) "${h}h ${m}m" else if (m > 0) "${m}m ${sec}s" else "${sec}s"
                                }
                            } ?: ""

                            GardenBed(
                                slot = slot,
                                slotNumber = slotIndex + 1,
                                progress = progress,
                                isReady = isReady,
                                timeRemaining = timeRemaining,
                                canWater = slot != null && !isReady,
                                canFertilize = slot != null && !isReady,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    if (slot == null && selectedFruit == null) {
                                        triggerShake()
                                        showSelectFruit()
                                    } else if (slot == null && selectedFruit != null) {
                                        appState.plantFruitInSlot(slotIndex, selectedFruit!!)
                                    }
                                },
                                onHarvest = {
                                    appState.harvestSlot(slotIndex)
                                },
                                onWater = {
                                    if (!appState.waterSlot(slotIndex)) showNotEnoughPoints()
                                },
                                onFertilize = {
                                    if (!appState.fertilizeSlot(slotIndex)) showNotEnoughPoints()
                                },
                                onRemove = {
                                    appState.removeFruitFromSlot(slotIndex)
                                },
                            )
                        }
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Hint
        if (selectedFruit != null && appState.occupiedSlots < appState.gardenSlots.size) {
            item {
                Text(
                    text = "Tap an empty bed to plant $selectedFruit",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGreen,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Leaderboard
        item {
            var showLeaderboard by remember { mutableStateOf(false) }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Button(
                    onClick = { showLeaderboard = !showLeaderboard },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedOrange,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = if (showLeaderboard) "Hide Leaderboard" else "\uD83C\uDFC6 Best Gardens",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                if (showLeaderboard) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LeaderboardList(userScore = appState.gardenScore.intValue, userName = appState.userName.value)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
    ) { data ->
        Snackbar(
            snackbarData = data,
            shape = RoundedCornerShape(14.dp),
            containerColor = SoftBerry,
            contentColor = Color.White,
        )
    }
    }
}

@Composable
private fun GardenBed(
    slot: GardenSlot?,
    slotNumber: Int,
    progress: Float,
    isReady: Boolean,
    timeRemaining: String,
    canWater: Boolean = false,
    canFertilize: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onHarvest: () -> Unit,
    onWater: () -> Unit = {},
    onFertilize: () -> Unit = {},
    onRemove: () -> Unit,
) {
    // Animation states
    var effectEmoji by remember { mutableStateOf<String?>(null) }
    val effectAlpha = remember { Animatable(0f) }
    val effectOffset = remember { Animatable(0f) }
    val effectScale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    val playEffect: (String) -> Unit = { emoji ->
        coroutineScope.launch {
            effectEmoji = emoji
            effectAlpha.snapTo(1f)
            effectOffset.snapTo(0f)
            effectScale.snapTo(1f)
            launch { effectAlpha.animateTo(0f, tween(800)) }
            launch { effectOffset.animateTo(-40f, tween(800, easing = EaseOut)) }
            launch { effectScale.animateTo(1.8f, tween(800, easing = EaseOut)) }
        }
    }

    // Harvest bounce
    val harvestScale = remember { Animatable(1f) }

    Card(
        modifier = modifier
            .height(165.dp)
            .shadow(if (slot != null) 4.dp else 1.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    if (isReady) {
                        coroutineScope.launch {
                            playEffect("⭐")
                            harvestScale.animateTo(1.2f, tween(100))
                            harvestScale.animateTo(0.8f, tween(100))
                            harvestScale.animateTo(1f, tween(100))
                            onHarvest()
                        }
                    } else {
                        onClick()
                    }
                }),
        ) {
            if (slot != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top — fruit area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                if (isReady) Brush.verticalGradient(
                                    listOf(harvestGold.copy(alpha = 0.3f), Color(0xFFFFF8E1)),
                                ) else Brush.verticalGradient(
                                    listOf(Color(0xFFE8F5E9), grassGreen.copy(alpha = 0.4f)),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.scale(harvestScale.value),
                        ) {
                            FruitIcon(name = slot.fruitName, size = 40.dp, emojiFontSize = 34.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = Translations.fruit(slot.fruitName, LocalLanguage.current),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                            )
                            if (isReady) {
                                Text(
                                    text = "⭐ +${FruitGrowTimes.getPoints(slot.fruitName)} pts",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoftBerry,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else {
                                Text(
                                    text = timeRemaining,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        // Floating effect emoji
                        if (effectEmoji != null) {
                            Text(
                                text = effectEmoji!!,
                                fontSize = 28.sp,
                                modifier = Modifier
                                    .offset(y = effectOffset.value.dp)
                                    .scale(effectScale.value)
                                    .alpha(effectAlpha.value),
                            )
                        }
                    }

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Color(0xFFE0E0E0)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(if (isReady) harvestGold else SoftGreen),
                        )
                    }

                    // Soil part
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (!isReady) 48.dp else 36.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(soilLight, soilDark),
                                ),
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                if (isReady) {
                                    Text(
                                        text = "\uD83C\uDF3E Harvest!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = harvestGold,
                                        fontWeight = FontWeight.Bold,
                                    )
                                } else {
                                    Text(
                                        text = "\uD83C\uDF3F ${(progress * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                                        .clickable(onClick = onRemove),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("✕", fontSize = 8.sp, color = Color.White)
                                }
                            }
                            if (!isReady) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    BoosterMiniButton(
                                        text = "\uD83D\uDCA7",
                                        enabled = canWater,
                                        onClick = { playEffect("\uD83D\uDCA7"); onWater() },
                                        modifier = Modifier.weight(1f),
                                    )
                                    BoosterMiniButton(
                                        text = "\uD83E\uDEB4",
                                        enabled = canFertilize,
                                        onClick = { playEffect("\uD83E\uDEB4"); onFertilize() },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Empty bed
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(2.dp, SoftGreenLight.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "\uD83C\uDF31", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bed $slotNumber",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "Tap to plant",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoftGreen,
                            )
                        }
                    }
                    // Empty soil
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Color(0xFFE0E0E0)),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(soilBed, soilLight.copy(alpha = 0.6f)),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("empty", style = MaterialTheme.typography.labelSmall, color = soilDark.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

private val lockedOverlay = Color(0xFF9E9E9E)

@Composable
private fun LockedBed(
    slotNumber: Int,
    cost: Int,
    canAfford: Boolean,
    modifier: Modifier = Modifier,
    onUnlock: () -> Unit,
) {
    Card(
        modifier = modifier.height(165.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onUnlock),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(lockedOverlay.copy(alpha = 0.12f))
                        .border(2.dp, lockedOverlay.copy(alpha = 0.2f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "\uD83D\uDD12", fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bed $slotNumber",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (canAfford) SoftBerry else lockedOverlay.copy(alpha = 0.2f),
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "⭐ $cost to unlock",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (canAfford) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(lockedOverlay.copy(alpha = 0.15f)),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    soilBed.copy(alpha = 0.5f),
                                    soilLight.copy(alpha = 0.3f),
                                ),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (canAfford) "Tap to unlock" else "locked",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (canAfford) SoftBerry else soilDark.copy(alpha = 0.4f),
                        fontWeight = if (canAfford) FontWeight.Medium else FontWeight.Normal,
                    )
                }
            }
        }
    }
}

@Composable
private fun BoosterCard(
    emoji: String,
    name: String,
    description: String,
    cost: Int,
    canAfford: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "⭐ $cost pts",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (canAfford) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun BoosterMiniButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(18.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (enabled) Color.White.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, fontSize = 11.sp)
    }
}

private data class LeaderboardEntry(val name: String, val score: Int, val harvested: Int, val isUser: Boolean = false)

private val fakeGardeners = listOf(
    LeaderboardEntry("Olivia Green", 2850, 64),
    LeaderboardEntry("Mango Mike", 2340, 51),
    LeaderboardEntry("Tropical Tina", 1980, 45),
    LeaderboardEntry("Berry Bob", 1750, 39),
    LeaderboardEntry("Citrus Clara", 1520, 34),
    LeaderboardEntry("Peachy Pete", 1310, 30),
    LeaderboardEntry("Kiwi Kate", 1150, 27),
    LeaderboardEntry("Grape Gary", 980, 22),
    LeaderboardEntry("Plum Patricia", 820, 19),
    LeaderboardEntry("Cherry Charlie", 700, 16),
    LeaderboardEntry("Lemon Lucy", 580, 14),
    LeaderboardEntry("Banana Bill", 470, 11),
    LeaderboardEntry("Fig Fiona", 380, 9),
    LeaderboardEntry("Melon Mary", 290, 7),
    LeaderboardEntry("Pear Paul", 220, 6),
    LeaderboardEntry("Apple Annie", 160, 4),
    LeaderboardEntry("Watermelon Walt", 110, 3),
    LeaderboardEntry("Coconut Chris", 70, 2),
    LeaderboardEntry("Guava Grace", 35, 1),
    LeaderboardEntry("Newbie Nick", 10, 1),
)

@Composable
private fun LeaderboardList(userScore: Int, userName: String) {
    val displayName = userName.ifBlank { "You" }
    val userEntry = LeaderboardEntry(displayName, userScore, 0, isUser = true)

    val combined = (fakeGardeners + userEntry).sortedByDescending { it.score }.take(21)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\uD83C\uDFC6 Leaderboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            combined.forEachIndexed { index, entry ->
                val rank = index + 1
                val medal = when (rank) {
                    1 -> "\uD83E\uDD47"
                    2 -> "\uD83E\uDD48"
                    3 -> "\uD83E\uDD49"
                    else -> null
                }
                val bgColor = if (entry.isUser) SoftGreenLight.copy(alpha = 0.5f)
                else if (rank <= 3) WarmYellow.copy(alpha = 0.15f)
                else Color.Transparent

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = medal ?: "#$rank",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(36.dp),
                        fontSize = if (medal != null) 18.sp else 14.sp,
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (entry.isUser) "\uD83C\uDF1F $displayName" else entry.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (entry.isUser) FontWeight.Bold else FontWeight.Medium,
                            color = if (entry.isUser) SoftBerry else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Text(
                        text = "⭐ ${entry.score}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (entry.isUser) SoftBerry else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
