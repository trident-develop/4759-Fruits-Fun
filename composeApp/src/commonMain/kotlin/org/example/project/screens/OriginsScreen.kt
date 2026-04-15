package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
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
import org.example.project.data.Country
import org.example.project.data.CountryFruit
import org.example.project.data.MockData
import org.example.project.data.Translations
import org.example.project.theme.*
import org.example.project.theme.FruitIcon

@Composable
fun OriginsScreen(onFruitClick: (String) -> Unit = {}) {
    val lang = LocalLanguage.current
    val countries = MockData.countries
    val pagerState = rememberPagerState(pageCount = { countries.size })
    val currentCountry by remember { derivedStateOf { countries[pagerState.currentPage] } }

    var selectedMonth by remember { mutableIntStateOf(4) } // April by default
    var showSeasonalOnly by remember { mutableStateOf(true) }

    val filteredFruits = remember(currentCountry, selectedMonth, showSeasonalOnly) {
        if (showSeasonalOnly) {
            currentCountry.fruits.filter { selectedMonth in it.seasonMonths }
        } else {
            currentCountry.fruits.filter { selectedMonth !in it.seasonMonths }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "\uD83C\uDF0D " + Strings.get("origins", LocalLanguage.current),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp),
        )
        Text(
            text = Strings.get("discover_origins", LocalLanguage.current),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.height(140.dp),
        ) { page ->
            CountryCard(country = countries[page])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(countries.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(if (index == pagerState.currentPage) 8.dp else 5.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pagerState.currentPage) SoftBerry
                            else SoftBerry.copy(alpha = 0.2f),
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Month selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            (1..12).forEach { month ->
                
                val isSelected = month == selectedMonth
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SoftBerry else SoftPink.copy(alpha = 0.25f))
                        .clickable { selectedMonth = month }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = Strings.monthName(month, lang),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Seasonal / Off-season toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = showSeasonalOnly,
                onClick = { showSeasonalOnly = true },
                label = { Text("☀\uFE0F ${Strings.get("in_season", lang)}") },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SoftGreen,
                    selectedLabelColor = Color.White,
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = !showSeasonalOnly,
                onClick = { showSeasonalOnly = false },
                label = { Text("\uD83C\uDF28\uFE0F ${Strings.get("off_season", lang)}") },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedLabelColor = Color.White,
                ),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (filteredFruits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (showSeasonalOnly)
                        "No fruits in season in ${Strings.monthName(selectedMonth, lang)}"
                    else
                        "All fruits are in season in ${Strings.monthName(selectedMonth, lang)}!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(filteredFruits) { fruit ->
                    FruitOriginCard(
                        fruit = fruit,
                        color = currentCountry.color,
                        isSeasonal = selectedMonth in fruit.seasonMonths,
                        onClick = { onFruitClick(fruit.name) },
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun CountryCard(country: Country) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = country.color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(country.color, country.color.copy(alpha = 0.5f)),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = country.flag, fontSize = 48.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = Translations.country(country.name, LocalLanguage.current),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${country.fruits.size} fruits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun FruitOriginCard(fruit: CountryFruit, color: Color, isSeasonal: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                FruitIcon(name = fruit.name, fallbackEmoji = fruit.emoji, size = 30.dp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Translations.fruit(fruit.name, LocalLanguage.current),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = fruit.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = if (isSeasonal) "☀\uFE0F" else "\uD83C\uDF28\uFE0F",
                fontSize = 16.sp,
            )
        }
    }
}
