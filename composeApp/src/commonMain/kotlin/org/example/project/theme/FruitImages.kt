package org.example.project.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fruitsfun.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val fruitResourceMap: Map<String, DrawableResource> = mapOf(
    "banana" to Res.drawable.banana,
    "cherry" to Res.drawable.cherry,
    "grape" to Res.drawable.grape,
    "kiwi" to Res.drawable.kiwi,
    "lemon" to Res.drawable.lemon,
    "pear" to Res.drawable.pear,
    "plum" to Res.drawable.plum,
    "watermelon" to Res.drawable.watermelon,
)

private val fruitEmojiMap: Map<String, String> = mapOf(
    "apple" to "\uD83C\uDF4E",          // 🍎
    "banana" to "\uD83C\uDF4C",          // 🍌
    "orange" to "\uD83C\uDF4A",          // 🍊
    "kiwi" to "\uD83E\uDD5D",            // 🥝
    "strawberry" to "\uD83C\uDF53",       // 🍓
    "pineapple" to "\uD83C\uDF4D",        // 🍍
    "peach" to "\uD83C\uDF51",            // 🍑
    "pear" to "\uD83C\uDF50",             // 🍐
    "watermelon" to "\uD83C\uDF49",       // 🍉
    "cherry" to "\uD83C\uDF52",           // 🍒
    "grape" to "\uD83C\uDF47",            // 🍇
    "lemon" to "\uD83C\uDF4B",            // 🍋
    "coconut" to "\uD83E\uDD65",          // 🥥
    "avocado" to "\uD83E\uDD51",          // 🥑
    "blueberry" to "\uD83E\uDED0",        // 🫐
    "fig" to "\uD83E\uDED2",              // 🫒 (closest)
    "melon" to "\uD83C\uDF48",            // 🍈
    "guava" to "\uD83C\uDF4F",            // 🍏
    "passion fruit" to "\uD83D\uDC9B",    // 💛
    "dragon fruit" to "\uD83D\uDC9C",     // 💜
    "mango" to "\uD83E\uDD6D",            // 🥭
    "plum" to "\uD83C\uDF46",             // 🍆 (closest purple)
    "pomegranate" to "\u2764\uFE0F",      // ❤️
    "papaya" to "\uD83E\uDED1",           // 🫑 (closest)
    "raspberry" to "\uD83D\uDD34",        // 🔴
    "lychee" to "\u26AA",                 // ⚪
    "mandarin" to "\uD83C\uDF4A",         // 🍊 (same family)
    "nectarine" to "\uD83C\uDF51",        // 🍑 (same family)
    "apricot" to "\uD83D\uDFE0",          // 🟠
    "jackfruit" to "\uD83D\uDFE1",        // 🟡
    "durian" to "\uD83D\uDFE2",           // 🟢
    "rambutan" to "\uD83D\uDD35",         // 🔵
    "mangosteen" to "\uD83D\uDFE3",       // 🟣
    "longan" to "\uD83D\uDFE4",           // 🟤
    "persimmon" to "\uD83C\uDF36\uFE0F",  // 🌶️ (orange/red)
    "cranberry" to "\uD83D\uDD3A",        // 🔺
)

fun fruitResource(name: String): DrawableResource? =
    fruitResourceMap[name.lowercase()]

fun fruitEmoji(name: String): String =
    fruitEmojiMap[name.lowercase()] ?: "\uD83C\uDF53"

fun hasDistinctIcon(name: String): Boolean =
    fruitResourceMap.containsKey(name.lowercase()) || name.lowercase() in setOf(
        "apple", "orange", "strawberry", "pineapple", "peach", "coconut",
        "avocado", "blueberry", "fig", "melon", "guava", "passion fruit",
        "dragon fruit", "mango",
    )

@Composable
fun FruitIcon(
    name: String,
    fallbackEmoji: String = fruitEmoji(name),
    size: Dp = 28.dp,
    emojiFontSize: TextUnit = 22.sp,
) {
    val res = fruitResource(name)
    if (res != null) {
        Image(
            painter = painterResource(res),
            contentDescription = name,
            modifier = Modifier.size(size),
            contentScale = ContentScale.Fit,
        )
    } else {
        Text(text = fallbackEmoji, fontSize = emojiFontSize)
    }
}
