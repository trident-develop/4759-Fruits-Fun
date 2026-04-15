package org.example.project.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fruitsfun.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

val SoftGreen = Color(0xFFA8D5A2)
val SoftGreenLight = Color(0xFFD4EDCF)
val Peach = Color(0xFFFFCBA4)
val PeachLight = Color(0xFFFFE5D0)
val WarmYellow = Color(0xFFFFF3B0)
val MutedOrange = Color(0xFFFFB347)
val LightCoral = Color(0xFFFF9A8B)
val Cream = Color(0xFFFFF8F0)
val CreamDark = Color(0xFFFFF0E0)
val SoftPink = Color(0xFFFFD1DC)
val SoftLavender = Color(0xFFE8D5F5)
val MintGreen = Color(0xFFB8E6C8)
val SoftBerry = Color(0xFFD4748A)
val WarmBrown = Color(0xFF8B6F5E)
val DarkText = Color(0xFF3D2C2E)
val SubtleText = Color(0xFF7A6B6E)

enum class AppTheme { Light, Dark, Fruity }

val LocalAppTheme = compositionLocalOf { AppTheme.Light }

private val LightColorScheme = lightColorScheme(
    primary = SoftBerry,
    onPrimary = Color.White,
    primaryContainer = SoftPink,
    onPrimaryContainer = DarkText,
    secondary = SoftGreen,
    onSecondary = Color.White,
    secondaryContainer = SoftGreenLight,
    onSecondaryContainer = DarkText,
    tertiary = MutedOrange,
    onTertiary = Color.White,
    tertiaryContainer = WarmYellow,
    onTertiaryContainer = DarkText,
    background = Cream,
    onBackground = DarkText,
    surface = Color.White,
    onSurface = DarkText,
    surfaceVariant = CreamDark,
    onSurfaceVariant = SubtleText,
    outline = Color(0xFFCBB8AB),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE8919F),
    onPrimary = Color(0xFF1E1012),
    primaryContainer = Color(0xFF3D2230),
    onPrimaryContainer = Color(0xFFFFD9E0),
    secondary = Color(0xFF9CD8A4),
    onSecondary = Color(0xFF0E1F10),
    secondaryContainer = Color(0xFF1E3822),
    onSecondaryContainer = Color(0xFFD4F5D8),
    tertiary = Color(0xFFFFCC80),
    onTertiary = Color(0xFF1E1508),
    tertiaryContainer = Color(0xFF3A2D14),
    onTertiaryContainer = Color(0xFFFFE8C4),
    background = Color(0xFF161218),
    onBackground = Color(0xFFE8DFE2),
    surface = Color(0xFF1E1A20),
    onSurface = Color(0xFFE8DFE2),
    surfaceVariant = Color(0xFF2C2630),
    onSurfaceVariant = Color(0xFFC4B8BF),
    outline = Color(0xFF514650),
    inverseSurface = Color(0xFFE8DFE2),
    inverseOnSurface = Color(0xFF1E1A20),
    surfaceContainerHigh = Color(0xFF28222C),
    surfaceContainer = Color(0xFF221C26),
)

private val FruityColorScheme = lightColorScheme(
    primary = Color(0xFFC95E73),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFB8C6),
    onPrimaryContainer = Color(0xFF3D1520),
    secondary = Color(0xFF6DB86E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB4E6B5),
    onSecondaryContainer = Color(0xFF153016),
    tertiary = Color(0xFFE89B30),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDFA0),
    onTertiaryContainer = Color(0xFF3A2508),
    background = Color(0xFFFFF5EC),
    onBackground = Color(0xFF2E1F20),
    surface = Color(0xFFFFFBF8),
    onSurface = Color(0xFF2E1F20),
    surfaceVariant = Color(0xFFFFECD8),
    onSurfaceVariant = Color(0xFF6A5558),
    outline = Color(0xFFD4A88E),
)

private val FruitTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp),
)

private val FruitShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun FruitTheme(
    theme: AppTheme = AppTheme.Light,
    language: AppLanguage = AppLanguage.English,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (theme) {
        AppTheme.Light -> LightColorScheme
        AppTheme.Fruity -> FruityColorScheme
        AppTheme.Dark -> DarkColorScheme
    }

    CompositionLocalProvider(
        LocalAppTheme provides theme,
        LocalLanguage provides language,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FruitTypography,
            shapes = FruitShapes,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
                if (theme == AppTheme.Fruity) {
                    FruityBackground()
                }
            }
        }
    }
}

private data class FloatingFruit(
    val res: DrawableResource,
    val startX: Float,
    val startY: Float,
    val driftX: Float,
    val driftY: Float,
    val durationMs: Int,
    val size: Float,
    val rotRange: Float = 15f,
)

private val floatingFruits = listOf(
    FloatingFruit(Res.drawable.banana, 0.02f, 0.05f, 0.04f, 0.05f, 6000, 50f, 20f),
    FloatingFruit(Res.drawable.cherry, 0.82f, 0.08f, -0.05f, 0.04f, 7000, 40f, 12f),
    FloatingFruit(Res.drawable.grape, 0.12f, 0.32f, 0.06f, -0.04f, 8000, 46f, 18f),
    FloatingFruit(Res.drawable.kiwi, 0.72f, 0.42f, -0.04f, 0.06f, 5500, 42f, 15f),
    FloatingFruit(Res.drawable.lemon, 0.45f, 0.02f, 0.05f, 0.05f, 7500, 38f, 22f),
    FloatingFruit(Res.drawable.pear, 0.88f, 0.62f, -0.06f, -0.04f, 6500, 48f, 14f),
    FloatingFruit(Res.drawable.plum, 0.08f, 0.68f, 0.05f, -0.05f, 5000, 36f, 20f),
    FloatingFruit(Res.drawable.watermelon, 0.55f, 0.78f, -0.04f, -0.06f, 8500, 54f, 10f),
    FloatingFruit(Res.drawable.banana, 0.40f, 0.55f, -0.03f, 0.04f, 9000, 34f, 25f),
    FloatingFruit(Res.drawable.cherry, 0.25f, 0.88f, 0.05f, -0.03f, 6200, 38f, 16f),
    FloatingFruit(Res.drawable.lemon, 0.70f, 0.25f, 0.04f, 0.03f, 7800, 32f, 18f),
    FloatingFruit(Res.drawable.grape, 0.92f, 0.45f, -0.05f, 0.05f, 5800, 36f, 12f),
)

@Composable
private fun FruityBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    Box(modifier = Modifier.fillMaxSize()) {
        floatingFruits.forEach { fruit ->
            val offsetX by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = fruit.driftX * 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(fruit.durationMs, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse,
                ),
            )
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = fruit.driftY * 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween((fruit.durationMs * 1.3f).toInt(), easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse,
                ),
            )
            val rotation by infiniteTransition.animateFloat(
                initialValue = -fruit.rotRange,
                targetValue = fruit.rotRange,
                animationSpec = infiniteRepeatable(
                    animation = tween((fruit.durationMs * 0.8f).toInt(), easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse,
                ),
            )

            Image(
                painter = painterResource(fruit.res),
                contentDescription = null,
                modifier = Modifier
                    .size(fruit.size.dp)
                    .offset(
                        x = (fruit.startX * 360f + offsetX).dp,
                        y = (fruit.startY * 700f + offsetY).dp,
                    )
                    .graphicsLayer { rotationZ = rotation }
                    .alpha(0.16f),
            )
        }
    }
}
