package org.example.project.screensgray

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fruitsfun.composeapp.generated.resources.Res
import fruitsfun.composeapp.generated.resources.banana
import fruitsfun.composeapp.generated.resources.cherry
import fruitsfun.composeapp.generated.resources.grape
import fruitsfun.composeapp.generated.resources.kiwi
import fruitsfun.composeapp.generated.resources.lemon
import fruitsfun.composeapp.generated.resources.pear
import fruitsfun.composeapp.generated.resources.plum
import fruitsfun.composeapp.generated.resources.watermelon
import kotlinx.coroutines.delay
import org.example.project.theme.Cream
import org.example.project.theme.MintGreen
import org.example.project.theme.MutedOrange
import org.example.project.theme.PeachLight
import org.example.project.theme.SoftBerry
import org.example.project.theme.SoftGreen
import org.example.project.theme.SoftGreenLight
import org.example.project.theme.SoftLavender
import org.example.project.theme.SoftPink
import org.example.project.theme.WarmYellow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class OrbitFruit(
    val res: DrawableResource,
    val size: Dp,
    val radius: Dp,
    val speed: Float,
    val startAngle: Float,
)

@Composable
fun LoadingScreen(onLoadingComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onLoadingComplete()
    }

    val infiniteTransition = rememberInfiniteTransition()

    // Center fruit animations
    val centerScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val centerRotate by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    // Inner orbit (fast, small fruits)
    val innerAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing)),
    )
    // Outer orbit (slow, bigger fruits, opposite direction)
    val outerAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = tween(5000, easing = LinearEasing)),
    )

    // Floating background blobs
    val blobOffset1 by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val blobOffset2 by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    // Title entrance
    val titleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    // Progress dots
    val dotPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(animation = tween(1200, easing = LinearEasing)),
    )

    val innerFruits = listOf(
        OrbitFruit(Res.drawable.cherry, 30.dp, 58.dp, 1f, 0f),
        OrbitFruit(Res.drawable.lemon, 28.dp, 58.dp, 1f, 120f),
        OrbitFruit(Res.drawable.grape, 30.dp, 58.dp, 1f, 240f),
    )
    val outerFruits = listOf(
        OrbitFruit(Res.drawable.kiwi, 36.dp, 95.dp, 1f, 0f),
        OrbitFruit(Res.drawable.pear, 34.dp, 95.dp, 1f, 72f),
        OrbitFruit(Res.drawable.watermelon, 38.dp, 95.dp, 1f, 144f),
        OrbitFruit(Res.drawable.plum, 32.dp, 95.dp, 1f, 216f),
        OrbitFruit(Res.drawable.cherry, 28.dp, 95.dp, 1f, 288f),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        PeachLight.copy(alpha = 0.6f),
                        Cream,
                        SoftGreenLight.copy(alpha = 0.7f),
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Floating decorative blobs
        Box(
            modifier = Modifier
                .offset(x = (-80).dp + blobOffset1.dp, y = (-200).dp + blobOffset2.dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(SoftPink.copy(alpha = 0.2f))
                .blur(40.dp),
        )
        Box(
            modifier = Modifier
                .offset(x = 90.dp + blobOffset2.dp, y = (-120).dp + blobOffset1.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(WarmYellow.copy(alpha = 0.25f))
                .blur(30.dp),
        )
        Box(
            modifier = Modifier
                .offset(x = (-60).dp + blobOffset2.dp, y = 180.dp + blobOffset1.dp)
                .size(120.dp)
                .clip(CircleShape)
                .background(MintGreen.copy(alpha = 0.2f))
                .blur(35.dp),
        )
        Box(
            modifier = Modifier
                .offset(x = 100.dp + blobOffset1.dp, y = 140.dp)
                .size(90.dp)
                .clip(CircleShape)
                .background(SoftLavender.copy(alpha = 0.2f))
                .blur(30.dp),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(250.dp),
            ) {
                // Glow behind center fruit
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(centerScale * 1.3f)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    WarmYellow.copy(alpha = 0.5f),
                                    WarmYellow.copy(alpha = 0f),
                                ),
                            ),
                        ),
                )

                // Outer orbit
                outerFruits.forEachIndexed { index, fruit ->
                    val angle = outerAngle + fruit.startAngle
                    val rad = angle.toDouble() * kotlin.math.PI / 180.0
                    val x = (fruit.radius.value * kotlin.math.cos(rad)).dp
                    val y = (fruit.radius.value * kotlin.math.sin(rad)).dp
                    // Each fruit bobs individually
                    val bobOffset = kotlin.math.sin((innerAngle + index * 90f).toDouble() * kotlin.math.PI / 180.0) * 4f

                    Image(
                        painter = painterResource(fruit.res),
                        contentDescription = null,
                        modifier = Modifier
                            .size(fruit.size)
                            .offset(x = x, y = y + bobOffset.dp)
                            .graphicsLayer {
                                rotationZ = angle * 0.3f
                                alpha = 0.9f
                            },
                    )
                }

                // Inner orbit
                innerFruits.forEachIndexed { index, fruit ->
                    val angle = innerAngle + fruit.startAngle
                    val rad = angle.toDouble() * kotlin.math.PI / 180.0
                    val x = (fruit.radius.value * kotlin.math.cos(rad)).dp
                    val y = (fruit.radius.value * kotlin.math.sin(rad)).dp
                    val bobOffset = kotlin.math.sin((outerAngle + index * 60f).toDouble() * kotlin.math.PI / 180.0) * 3f

                    Image(
                        painter = painterResource(fruit.res),
                        contentDescription = null,
                        modifier = Modifier
                            .size(fruit.size)
                            .offset(x = x, y = y + bobOffset.dp)
                            .graphicsLayer {
                                rotationZ = -angle * 0.5f
                            },
                    )
                }

                // Center banana
                Image(
                    painter = painterResource(Res.drawable.banana),
                    contentDescription = null,
                    modifier = Modifier
                        .size(76.dp)
                        .scale(centerScale)
                        .rotate(centerRotate),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Discover delicious fruit recipes",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Bouncing dots loader
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(3) { index ->
                    val dotScale = if ((dotPhase.toInt() % 3) == index) 1.4f else 0.8f
                    val dotColor = when (index) {
                        0 -> SoftBerry
                        1 -> MutedOrange
                        else -> SoftGreen
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .scale(dotScale)
                            .clip(CircleShape)
                            .background(dotColor),
                    )
                }
            }
        }
    }
}