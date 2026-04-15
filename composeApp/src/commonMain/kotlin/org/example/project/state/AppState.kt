@file:OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)

package org.example.project.state

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import org.example.project.AppPreferences
import org.example.project.data.MockData
import org.example.project.data.Recipe
import org.example.project.data.RecipeCategory
import org.example.project.theme.AppLanguage
import org.example.project.theme.AppTheme

data class GardenSlot(
    val fruitName: String,
    val plantedAtMs: Long,
    val growDurationMs: Long,
) {
    fun encode(): String = "$fruitName|$plantedAtMs|$growDurationMs"

    companion object {
        fun decode(s: String): GardenSlot? {
            val parts = s.split("|")
            if (parts.size != 3) return null
            return GardenSlot(
                fruitName = parts[0],
                plantedAtMs = parts[1].toLongOrNull() ?: return null,
                growDurationMs = parts[2].toLongOrNull() ?: return null,
            )
        }
    }
}

object FruitGrowTimes {
    private val growMinutes: Map<String, Int> = mapOf(
        "strawberry" to 10,
        "cherry" to 15,
        "lemon" to 20,
        "grape" to 25,
        "blueberry" to 12,
        "raspberry" to 14,
        "kiwi" to 30,
        "plum" to 35,
        "peach" to 40,
        "pear" to 45,
        "banana" to 60,
        "apple" to 90,
        "orange" to 120,
        "watermelon" to 180,
        "mango" to 240,
        "pineapple" to 300,
        "papaya" to 360,
        "coconut" to 480,
        "avocado" to 600,
        "jackfruit" to 720,
        "durian" to 1440,
    )

    fun getGrowMinutes(fruitName: String): Int =
        growMinutes[fruitName.lowercase()] ?: 60

    fun getGrowMs(fruitName: String): Long =
        getGrowMinutes(fruitName) * 60_000L

    fun getPoints(fruitName: String): Int {
        val minutes = getGrowMinutes(fruitName)
        return when {
            minutes <= 15 -> 5
            minutes <= 30 -> 10
            minutes <= 60 -> 20
            minutes <= 120 -> 35
            minutes <= 240 -> 50
            minutes <= 480 -> 75
            minutes <= 720 -> 100
            else -> 150
        }
    }
}

expect fun currentTimeMs(): Long

const val WATER_COST = 5
const val FERTILIZER_COST = 15

class AppState(private val prefs: AppPreferences? = null) {
    val selectedTabIndex = mutableStateOf(0)
    val favoriteRecipeIds = mutableStateListOf<Int>()
    val favoriteFruitNames = mutableStateListOf<String>()
    val userName = mutableStateOf("")
    val profileImageBytes = mutableStateOf<ByteArray?>(null)
    val appTheme = mutableStateOf(AppTheme.Light)
    val appLanguage = mutableStateOf(AppLanguage.English)

    // Calorie calculator
    val calcHeight = mutableStateOf("")
    val calcWeight = mutableStateOf("")
    val calcIsMale = mutableStateOf(true)
    val calcResult = mutableStateOf<Int?>(null)

    // Garden — 8 slots
    val gardenSlots = mutableStateListOf<GardenSlot?>(null, null, null, null, null, null, null, null, null, null)
    val gardenScore = mutableIntStateOf(0)
    val harvestedCount = mutableIntStateOf(0)
    val unlockedSlots = mutableIntStateOf(2)

    init {
        loadGarden()
        loadFavorites()
        loadUserName()
        loadProfileImage()
        loadCalcData()
    }

    val plantedFruits: List<String>
        get() = gardenSlots.filterNotNull().map { it.fruitName }

    val plantedFruitsGrouped: Map<String, Int>
        get() = plantedFruits.groupingBy { it }.eachCount()

    val occupiedSlots: Int
        get() = gardenSlots.count { it != null }

    fun isSlotUnlocked(index: Int): Boolean = index < unlockedSlots.intValue

    fun slotUnlockCost(index: Int): Int = when {
        index < 2 -> 0
        else -> (index - 1) * 50  // slot 2→50, slot 3→100, slot 4→150, ...
    }

    fun nextLockedSlotIndex(): Int? {
        val next = unlockedSlots.intValue
        return if (next < gardenSlots.size) next else null
    }

    fun unlockNextSlot(): Boolean {
        val next = nextLockedSlotIndex() ?: return false
        val cost = slotUnlockCost(next)
        if (gardenScore.intValue < cost) return false
        gardenScore.intValue -= cost
        unlockedSlots.intValue++
        saveGarden()
        return true
    }

    fun plantFruitInSlot(slotIndex: Int, fruitName: String): Boolean {
        if (slotIndex !in gardenSlots.indices) return false
        if (!isSlotUnlocked(slotIndex)) return false
        if (gardenSlots[slotIndex] != null) return false
        gardenSlots[slotIndex] = GardenSlot(
            fruitName = fruitName,
            plantedAtMs = currentTimeMs(),
            growDurationMs = FruitGrowTimes.getGrowMs(fruitName),
        )
        saveGarden()
        return true
    }

    fun waterSlot(slotIndex: Int): Boolean {
        val slot = gardenSlots.getOrNull(slotIndex) ?: return false
        if (gardenScore.intValue < WATER_COST) return false
        val elapsed = currentTimeMs() - slot.plantedAtMs
        if (elapsed >= slot.growDurationMs) return false
        gardenScore.intValue -= WATER_COST
        // Shift plantedAt earlier by 20% of total duration → speeds up
        val boost = (slot.growDurationMs * 0.20).toLong()
        gardenSlots[slotIndex] = slot.copy(plantedAtMs = slot.plantedAtMs - boost)
        saveGarden()
        return true
    }

    fun fertilizeSlot(slotIndex: Int): Boolean {
        val slot = gardenSlots.getOrNull(slotIndex) ?: return false
        if (gardenScore.intValue < FERTILIZER_COST) return false
        val elapsed = currentTimeMs() - slot.plantedAtMs
        if (elapsed >= slot.growDurationMs) return false
        gardenScore.intValue -= FERTILIZER_COST
        val boost = (slot.growDurationMs * 0.40).toLong()
        gardenSlots[slotIndex] = slot.copy(plantedAtMs = slot.plantedAtMs - boost)
        saveGarden()
        return true
    }

    fun removeFruitFromSlot(slotIndex: Int) {
        if (slotIndex in gardenSlots.indices) {
            gardenSlots[slotIndex] = null
            saveGarden()
        }
    }

    fun harvestSlot(slotIndex: Int): Int {
        val slot = gardenSlots.getOrNull(slotIndex) ?: return 0
        val elapsed = currentTimeMs() - slot.plantedAtMs
        if (elapsed < slot.growDurationMs) return 0
        val points = FruitGrowTimes.getPoints(slot.fruitName)
        gardenScore.intValue += points
        harvestedCount.intValue++
        gardenSlots[slotIndex] = null
        saveGarden()
        return points
    }

    private fun saveGarden() {
        val p = prefs ?: return
        for (i in gardenSlots.indices) {
            val encoded = gardenSlots[i]?.encode() ?: ""
            p.putString("garden_slot_$i", encoded)
        }
        p.putInt("garden_score", gardenScore.intValue)
        p.putInt("garden_harvested", harvestedCount.intValue)
        p.putInt("garden_unlocked", unlockedSlots.intValue)
    }

    private fun loadGarden() {
        val p = prefs ?: return
        for (i in gardenSlots.indices) {
            val encoded = p.getString("garden_slot_$i", "")
            gardenSlots[i] = if (encoded.isNotEmpty()) GardenSlot.decode(encoded) else null
        }
        gardenScore.intValue = p.getInt("garden_score", 0)
        harvestedCount.intValue = p.getInt("garden_harvested", 0)
        unlockedSlots.intValue = p.getInt("garden_unlocked", 2)
    }

    // Unique fruit list from all app data
    val allAvailableFruits: List<String> by lazy {
        val fromRecipes = MockData.categories.flatMap { cat ->
            cat.recipes.flatMap { it.fruitsUsed }
        }
        val fromCountries = MockData.countries.flatMap { country ->
            country.fruits.map { it.name }
        }
        (fromRecipes + fromCountries).distinct().sorted()
    }

    // User recipes
    private val userRecipes = mutableStateListOf<Pair<Int, Recipe>>()
    private var nextUserRecipeId = 90000

    init {
        loadUserRecipes()
    }

    val allCategories: List<RecipeCategory>
        get() {
            val userByCategory = userRecipes.groupBy({ it.first }, { it.second })
            return MockData.categories.map { category ->
                val extra = userByCategory[category.id].orEmpty()
                if (extra.isNotEmpty()) {
                    category.copy(recipes = category.recipes + extra)
                } else {
                    category
                }
            }
        }

    val allRecipes: List<Recipe>
        get() = allCategories.flatMap { it.recipes }

    fun getRecipeById(id: Int): Recipe? = allRecipes.find { it.id == id }

    fun isUserRecipe(recipeId: Int): Boolean =
        userRecipes.any { it.second.id == recipeId }

    fun getUserRecipeCategoryId(recipeId: Int): Int? =
        userRecipes.find { it.second.id == recipeId }?.first

    fun addUserRecipe(categoryId: Int, recipe: Recipe): Recipe {
        val finalRecipe = recipe.copy(id = nextUserRecipeId++)
        userRecipes.add(categoryId to finalRecipe)
        saveUserRecipes()
        return finalRecipe
    }

    fun deleteAllUserRecipes() {
        val userIds = userRecipes.map { it.second.id }
        userIds.forEach { favoriteRecipeIds.remove(it) }
        userRecipes.clear()
        saveUserRecipes()
        saveFavorites()
    }

    fun resetGarden() {
        for (i in gardenSlots.indices) {
            gardenSlots[i] = null
        }
        gardenScore.intValue = 0
        harvestedCount.intValue = 0
        unlockedSlots.intValue = 2
        saveGarden()
    }

    fun deleteUserRecipe(recipeId: Int) {
        val index = userRecipes.indexOfFirst { it.second.id == recipeId }
        if (index >= 0) {
            favoriteRecipeIds.remove(recipeId)
            userRecipes.removeAt(index)
            saveUserRecipes()
            saveFavorites()
        }
    }

    fun updateUserRecipe(recipeId: Int, categoryId: Int, recipe: Recipe) {
        val index = userRecipes.indexOfFirst { it.second.id == recipeId }
        if (index >= 0) {
            userRecipes[index] = categoryId to recipe.copy(id = recipeId)
            saveUserRecipes()
        }
    }

    private fun encodeRecipe(categoryId: Int, r: Recipe): String {
        val fruits = r.fruitsUsed.joinToString(";")
        val ingr = r.ingredients.joinToString(";")
        val stps = r.steps.joinToString(";")
        return "$categoryId§${r.id}§${r.title}§${r.emoji}§$fruits§$ingr§$stps§${r.prepTime}§${r.color.value}"
    }

    private fun decodeRecipe(s: String): Pair<Int, Recipe>? {
        val p = s.split("§")
        if (p.size != 9) return null
        val catId = p[0].toIntOrNull() ?: return null
        val id = p[1].toIntOrNull() ?: return null
        val recipe = Recipe(
            id = id,
            title = p[2],
            emoji = p[3],
            fruitsUsed = p[4].split(";").filter { it.isNotBlank() },
            ingredients = p[5].split(";").filter { it.isNotBlank() },
            steps = p[6].split(";").filter { it.isNotBlank() },
            prepTime = p[7],
            color = androidx.compose.ui.graphics.Color(p[8].toULong()),
        )
        return catId to recipe
    }

    private fun saveUserRecipes() {
        val pr = prefs ?: return
        pr.putInt("user_recipes_count", userRecipes.size)
        pr.putInt("user_recipes_next_id", nextUserRecipeId)
        userRecipes.forEachIndexed { i, (catId, recipe) ->
            pr.putString("user_recipe_$i", encodeRecipe(catId, recipe))
        }
    }

    private fun loadUserRecipes() {
        val pr = prefs ?: return
        val count = pr.getInt("user_recipes_count", 0)
        nextUserRecipeId = pr.getInt("user_recipes_next_id", 90000)
        userRecipes.clear()
        for (i in 0 until count) {
            val encoded = pr.getString("user_recipe_$i", "")
            if (encoded.isNotEmpty()) {
                decodeRecipe(encoded)?.let { userRecipes.add(it) }
            }
        }
    }

    fun toggleFavorite(recipeId: Int) {
        if (recipeId in favoriteRecipeIds) {
            favoriteRecipeIds.remove(recipeId)
        } else {
            favoriteRecipeIds.add(recipeId)
        }
        saveFavorites()
    }

    fun isFavorite(recipeId: Int): Boolean = recipeId in favoriteRecipeIds

    fun toggleFavoriteFruit(name: String) {
        if (name in favoriteFruitNames) {
            favoriteFruitNames.remove(name)
        } else {
            favoriteFruitNames.add(name)
        }
        saveFavorites()
    }

    fun isFruitFavorite(name: String): Boolean = name in favoriteFruitNames

    private fun saveFavorites() {
        val p = prefs ?: return
        p.putString("favorites", favoriteRecipeIds.joinToString(","))
        p.putString("favorite_fruits", favoriteFruitNames.joinToString(","))
    }

    private fun loadFavorites() {
        val p = prefs ?: return
        val raw = p.getString("favorites", "")
        if (raw.isNotEmpty()) {
            favoriteRecipeIds.clear()
            raw.split(",").mapNotNull { it.toIntOrNull() }.forEach { favoriteRecipeIds.add(it) }
        }
        val rawFruits = p.getString("favorite_fruits", "")
        if (rawFruits.isNotEmpty()) {
            favoriteFruitNames.clear()
            rawFruits.split(",").filter { it.isNotBlank() }.forEach { favoriteFruitNames.add(it) }
        }
    }

    fun setUserName(name: String) {
        userName.value = name
        prefs?.putString("user_name", name)
    }

    private fun loadUserName() {
        val p = prefs ?: return
        userName.value = p.getString("user_name", "")
    }

    fun saveCalcData(height: String, weight: String, isMale: Boolean, result: Int?) {
        calcHeight.value = height
        calcWeight.value = weight
        calcIsMale.value = isMale
        calcResult.value = result
        val p = prefs ?: return
        p.putString("calc_height", height)
        p.putString("calc_weight", weight)
        p.putString("calc_gender", if (isMale) "m" else "f")
        p.putString("calc_result", result?.toString() ?: "")
    }

    private fun loadCalcData() {
        val p = prefs ?: return
        calcHeight.value = p.getString("calc_height", "")
        calcWeight.value = p.getString("calc_weight", "")
        calcIsMale.value = p.getString("calc_gender", "m") == "m"
        val r = p.getString("calc_result", "")
        calcResult.value = r.toIntOrNull()
    }

    fun setProfileImage(bytes: ByteArray?) {
        profileImageBytes.value = bytes
        val p = prefs ?: return
        if (bytes != null) {
            p.putString("profile_image", bytesToBase64(bytes))
        } else {
            p.putString("profile_image", "")
        }
    }

    private fun loadProfileImage() {
        val p = prefs ?: return
        val b64 = p.getString("profile_image", "")
        profileImageBytes.value = if (b64.isNotEmpty()) base64ToBytes(b64) else null
    }

    fun setTheme(theme: AppTheme) {
        appTheme.value = theme
        prefs?.putString("app_theme", theme.name)
    }

    fun setLanguage(language: AppLanguage) {
        appLanguage.value = language
        prefs?.putString("app_language", language.code)
    }

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val p = prefs ?: return
        val themeName = p.getString("app_theme", "Light")
        appTheme.value = AppTheme.entries.find { it.name == themeName } ?: AppTheme.Light
        val langCode = p.getString("app_language", "en")
        appLanguage.value = AppLanguage.entries.find { it.code == langCode } ?: AppLanguage.English
    }
}

private fun bytesToBase64(bytes: ByteArray): String =
    kotlin.io.encoding.Base64.encode(bytes)

private fun base64ToBytes(str: String): ByteArray? =
    try { kotlin.io.encoding.Base64.decode(str) } catch (_: Exception) { null }
