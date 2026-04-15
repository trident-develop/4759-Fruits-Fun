package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.state.AppState
import org.example.project.theme.*

enum class Tab(val labelKey: String, val icon: String) {
    Recipes("recipes", "\uD83E\uDD64"),
    Origins("origins", "\uD83C\uDF0D"),
    MyGarden("my_garden", "\uD83C\uDF3F"),
    Profile("profile", "\uD83D\uDC64"),
}

@Composable
fun MainScreen(
    appState: AppState,
    onRecipeClick: (Int) -> Unit,
    onAddRecipeClick: () -> Unit,
    onEditRecipeClick: (Int) -> Unit,
    onFruitClick: (String) -> Unit,
    onSettingsClick: () -> Unit = {},
    onWebViewClick: (String, String) -> Unit = { _, _ -> },
) {
    val selectedTab = Tab.entries[appState.selectedTabIndex.value]

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FruitBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { appState.selectedTabIndex.value = it.ordinal },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                Tab.Recipes -> RecipesScreen(appState = appState, onRecipeClick = onRecipeClick, onAddRecipeClick = onAddRecipeClick)
                Tab.Origins -> OriginsScreen(onFruitClick = onFruitClick)
                Tab.MyGarden -> GardenScreen(appState = appState)
                Tab.Profile -> ProfileScreen(appState = appState, onWebViewClick = onWebViewClick, onRecipeClick = onRecipeClick, onFruitClick = onFruitClick)
            }

            // Settings gear icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 18.dp, end = 16.dp)
                    .size(36.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .clickable(onClick = onSettingsClick),
                contentAlignment = Alignment.Center,
            ) {
                Text("⚙\uFE0F", fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun FruitBottomBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Tab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Text(
                        text = tab.icon,
                        fontSize = if (isSelected) 24.sp else 20.sp,
                    )
                },
                label = {
                    Text(
                        text = Strings.get(tab.labelKey, LocalLanguage.current),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = when (tab) {
                        Tab.Recipes -> SoftGreenLight
                        Tab.Origins -> PeachLight
                        Tab.MyGarden -> MintGreen.copy(alpha = 0.4f)
                        Tab.Profile -> SoftPink.copy(alpha = 0.5f)
                    },
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}
