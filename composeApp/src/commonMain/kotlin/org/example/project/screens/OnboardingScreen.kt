package org.example.project.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.theme.*

private data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String,
    val gradientColors: List<Color>,
)

private data class PageData(val emoji: String, val titleKey: String, val descKey: String, val gradientColors: List<Color>)
private val pageData = listOf(
    PageData("\uD83C\uDF4F\uD83E\uDD64\uD83C\uDF70", "onboarding_title_1", "onboarding_desc_1", listOf(PeachLight, Cream, WarmYellow.copy(alpha = 0.4f))),
    PageData("\uD83C\uDF0D\uD83C\uDF4A\uD83C\uDF34", "onboarding_title_2", "onboarding_desc_2", listOf(SoftGreenLight, Cream, MintGreen.copy(alpha = 0.4f))),
    PageData("\uD83C\uDF31\uD83C\uDF3B\uD83C\uDF33", "onboarding_title_3", "onboarding_desc_3", listOf(SoftPink.copy(alpha = 0.5f), Cream, SoftLavender.copy(alpha = 0.4f))),
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val lang = LocalLanguage.current
    val pages = pageData.map { OnboardingPage(it.emoji, Strings.get(it.titleKey, lang), Strings.get(it.descKey, lang), it.gradientColors) }
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            OnboardingPageContent(pages[page])
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 32.dp),
            ) {
                repeat(pages.size) { index ->
                    val width by animateDpAsState(
                        targetValue = if (index == pagerState.currentPage) 24.dp else 8.dp,
                        animationSpec = tween(300),
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) SoftBerry
                                else SoftBerry.copy(alpha = 0.25f),
                            ),
                    )
                }
            }

            Button(
                onClick = {
                    if (isLastPage) {
                        onFinished()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftBerry,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
                Text(
                    text = if (isLastPage) Strings.get("get_started", lang) else Strings.get("next", lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            TextButton(
                onClick = if (!isLastPage) onFinished else ({}),
                modifier = Modifier.padding(top = 8.dp),
                enabled = !isLastPage,
            ) {
                Text(
                    text = if (!isLastPage) Strings.get("skip", lang) else "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(page.gradientColors)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .offset(y = (-40).dp),
        ) {
            Text(
                text = page.emoji,
                fontSize = 64.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
            )
        }
    }
}
