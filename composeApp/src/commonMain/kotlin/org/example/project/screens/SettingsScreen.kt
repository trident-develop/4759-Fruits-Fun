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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import org.example.project.state.AppState
import org.example.project.theme.*

@Composable
fun SettingsScreen(
    appState: AppState,
    onBack: () -> Unit,
) {
    val lang = appState.appLanguage.value

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 20.dp, top = 48.dp, bottom = 16.dp),
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
                text = "⚙\uFE0F ${Strings.get("settings", lang)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            // Theme section
            item {
                SectionTitle("\uD83C\uDF08 ${Strings.get("theme", lang)}")
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ThemeOption(
                            emoji = "☀\uFE0F",
                            name = Strings.get("light", lang),
                            description = Strings.get("light_desc", lang),
                            isSelected = appState.appTheme.value == AppTheme.Light,
                            onClick = { appState.setTheme(AppTheme.Light) },
                        )
                        ThemeOption(
                            emoji = "\uD83C\uDF19",
                            name = Strings.get("dark", lang),
                            description = Strings.get("dark_desc", lang),
                            isSelected = appState.appTheme.value == AppTheme.Dark,
                            onClick = { appState.setTheme(AppTheme.Dark) },
                        )
                        ThemeOption(
                            emoji = "\uD83C\uDF4E",
                            name = Strings.get("fruity", lang),
                            description = Strings.get("fruity_desc", lang),
                            isSelected = appState.appTheme.value == AppTheme.Fruity,
                            onClick = { appState.setTheme(AppTheme.Fruity) },
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Language section
            item {
                SectionTitle("\uD83C\uDF10 ${Strings.get("language", lang)}")
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        AppLanguage.entries.forEach { language ->
                            LanguageOption(
                                language = language,
                                isSelected = appState.appLanguage.value == language,
                                onClick = { appState.setLanguage(language) },
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Data section
            item {
                SectionTitle("\uD83D\uDDD1\uFE0F Data")
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DangerButton(
                        text = "\uD83D\uDCDD Delete all my recipes",
                        onClick = { appState.deleteAllUserRecipes() },
                    )
                    DangerButton(
                        text = "\uD83C\uDF31 Reset my garden",
                        onClick = { appState.resetGarden() },
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Fruits Fun v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
    )
}

@Composable
private fun ThemeOption(
    emoji: String,
    name: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) SoftBerry.copy(alpha = 0.1f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(emoji, fontSize = 26.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(SoftBerry),
                contentAlignment = Alignment.Center,
            ) {
                Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LanguageOption(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val flag = when (language) {
        AppLanguage.English -> "\uD83C\uDDEC\uD83C\uDDE7"
        AppLanguage.Spanish -> "\uD83C\uDDEA\uD83C\uDDF8"
        AppLanguage.French -> "\uD83C\uDDEB\uD83C\uDDF7"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) SoftGreen.copy(alpha = 0.1f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(flag, fontSize = 26.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = language.label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(SoftGreen),
                contentAlignment = Alignment.Center,
            ) {
                Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DangerButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFD32F2F),
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F).copy(alpha = 0.4f)),
    ) {
        Text(text = text, fontWeight = FontWeight.Medium)
    }
}
