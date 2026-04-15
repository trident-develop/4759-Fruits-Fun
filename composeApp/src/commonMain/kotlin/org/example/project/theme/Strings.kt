package org.example.project.theme

import androidx.compose.runtime.compositionLocalOf

enum class AppLanguage(val code: String, val label: String) {
    English("en", "English"),
    Spanish("es", "Español"),
    French("fr", "Français"),
}

val LocalLanguage = compositionLocalOf { AppLanguage.English }

private fun t(en: String, es: String, fr: String) = mapOf(
    AppLanguage.English to en, AppLanguage.Spanish to es, AppLanguage.French to fr,
)

object Strings {
    private val translations: Map<String, Map<AppLanguage, String>> = mapOf(
        // Tabs
        "recipes" to t("Recipes", "Recetas", "Recettes"),
        "origins" to t("Origins", "Orígenes", "Origines"),
        "my_garden" to t("My Garden", "Mi Jardín", "Mon Jardin"),
        "profile" to t("Profile", "Perfil", "Profil"),
        "settings" to t("Settings", "Ajustes", "Paramètres"),

        // Recipes screen
        "explore_recipes" to t("Explore delicious fruit recipes", "Explora deliciosas recetas de frutas", "Découvrez de délicieuses recettes de fruits"),
        "search_fruit" to t("Search by fruit...", "Buscar por fruta...", "Rechercher par fruit..."),
        "no_recipes_found" to t("No recipes found with", "No se encontraron recetas con", "Aucune recette trouvée avec"),
        "recipes_count" to t("recipes", "recetas", "recettes"),
        "fruits_count" to t("fruits", "frutas", "fruits"),
        "create_recipe" to t("Create Recipe", "Crear Receta", "Créer une Recette"),
        "edit_recipe" to t("Edit Recipe", "Editar Receta", "Modifier la Recette"),
        "add_recipe" to t("Add Recipe", "Agregar Receta", "Ajouter la Recette"),
        "save_changes" to t("Save Changes", "Guardar Cambios", "Enregistrer"),
        "category" to t("Category", "Categoría", "Catégorie"),
        "select_category" to t("Select category...", "Seleccionar categoría...", "Sélectionner catégorie..."),
        "recipe_title" to t("Recipe title", "Título de la receta", "Titre de la recette"),
        "fruits_used_label" to t("Fruits used (comma-separated)", "Frutas usadas (separadas por coma)", "Fruits utilisés (séparés par virgule)"),
        "ingredients_label" to t("Ingredients (one per line)", "Ingredientes (uno por línea)", "Ingrédients (un par ligne)"),
        "steps_label" to t("Steps (one per line)", "Pasos (uno por línea)", "Étapes (une par ligne)"),
        "prep_time" to t("Preparation time", "Tiempo de preparación", "Temps de préparation"),

        // Recipe details
        "fruits_used" to t("Fruits Used", "Frutas Usadas", "Fruits Utilisés"),
        "ingredients" to t("Ingredients", "Ingredientes", "Ingrédients"),
        "preparation" to t("Preparation", "Preparación", "Préparation"),
        "servings" to t("Servings", "Porciones", "Portions"),

        // Origins screen
        "discover_origins" to t("Discover where fruits come from", "Descubre de dónde vienen las frutas", "Découvrez d'où viennent les fruits"),
        "fruits_from" to t("Fruits from", "Frutas de", "Fruits de"),
        "in_season" to t("In Season", "En Temporada", "De Saison"),
        "off_season" to t("Off Season", "Fuera de Temporada", "Hors Saison"),
        "no_fruits_season" to t("No fruits in season in", "No hay frutas en temporada en", "Aucun fruit de saison en"),
        "all_fruits_season" to t("All fruits are in season in", "Todas las frutas están en temporada en", "Tous les fruits sont de saison en"),

        // Fruit details
        "origin_growing" to t("Origin & Growing", "Origen y Cultivo", "Origine & Culture"),
        "origin_region" to t("Origin region", "Región de origen", "Région d'origine"),
        "climate" to t("Climate", "Clima", "Climat"),
        "top_producers" to t("Top producers", "Principales productores", "Principaux producteurs"),
        "nutrition_per_100g" to t("Nutrition per 100g", "Nutrición por 100g", "Nutrition pour 100g"),
        "vitamins_minerals" to t("Vitamins & Minerals", "Vitaminas y Minerales", "Vitamines & Minéraux"),
        "compare_fruit" to t("Compare with another fruit", "Comparar con otra fruta", "Comparer avec un autre fruit"),
        "hide_comparison" to t("Hide Comparison", "Ocultar Comparación", "Masquer la Comparaison"),
        "select_compare" to t("Select a fruit to compare...", "Selecciona una fruta para comparar...", "Choisir un fruit à comparer..."),
        "leaderboard" to t("Leaderboard", "Clasificación", "Classement"),

        // Garden screen
        "your_garden" to t("Your Beautiful Garden", "Tu Hermoso Jardín", "Votre Beau Jardin"),
        "grow_paradise" to t("Grow your own fruit paradise", "Cultiva tu paraíso frutal", "Cultivez votre paradis fruité"),
        "points" to t("Points", "Puntos", "Points"),
        "harvested" to t("Harvested", "Cosechado", "Récolté"),
        "beds" to t("Beds", "Parcelas", "Parcelles"),
        "select_fruit" to t("Select a Fruit to Plant", "Selecciona una Fruta para Plantar", "Choisir un Fruit à Planter"),
        "choose_then_tap" to t("Choose a fruit, then tap an empty bed", "Elige una fruta, luego toca una parcela vacía", "Choisissez un fruit, puis touchez une parcelle vide"),
        "choose_fruit" to t("Choose a fruit...", "Elige una fruta...", "Choisir un fruit..."),
        "garden_beds" to t("Garden Beds", "Parcelas del Jardín", "Parcelles du Jardin"),
        "garden_shop" to t("Garden Shop", "Tienda del Jardín", "Boutique du Jardin"),
        "shop_hint" to t("Use boosters on growing fruits to speed them up", "Usa potenciadores en frutas para acelerar su crecimiento", "Utilisez des boosters pour accélérer la croissance"),
        "water" to t("Water", "Agua", "Eau"),
        "fertilizer" to t("Fertilizer", "Fertilizante", "Engrais"),
        "harvest" to t("Harvest!", "¡Cosechar!", "Récolter !"),
        "growing" to t("Growing", "Creciendo", "Pousse"),
        "tap_plant" to t("Tap to plant", "Toca para plantar", "Appuyez pour planter"),
        "tap_empty_bed" to t("Tap an empty bed to plant", "Toca una parcela vacía para plantar", "Touchez une parcelle vide pour planter"),
        "empty" to t("empty", "vacío", "vide"),
        "locked" to t("locked", "bloqueado", "verrouillé"),
        "tap_unlock" to t("Tap to unlock", "Toca para desbloquear", "Appuyez pour débloquer"),
        "to_unlock" to t("to unlock", "para desbloquear", "pour débloquer"),
        "bed" to t("Bed", "Parcela", "Parcelle"),
        "beds_planted" to t("beds planted", "parcelas plantadas", "parcelles plantées"),
        "garden_full" to t("All beds are planted! Your garden is thriving!", "¡Todas las parcelas están plantadas! ¡Tu jardín florece!", "Toutes les parcelles sont plantées ! Votre jardin prospère !"),
        "best_gardens" to t("Best Gardens", "Mejores Jardines", "Meilleurs Jardins"),
        "hide_leaderboard" to t("Hide Leaderboard", "Ocultar Clasificación", "Masquer le Classement"),
        "not_enough_points" to t("Not enough points! ⭐", "¡No hay suficientes puntos! ⭐", "Pas assez de points ! ⭐"),
        "select_fruit_first" to t("Select a fruit first! \uD83C\uDF4F", "¡Selecciona una fruta primero! \uD83C\uDF4F", "Choisissez un fruit d'abord ! \uD83C\uDF4F"),
        "speed" to t("Speed", "Velocidad", "Vitesse"),
        "ready" to t("Ready!", "¡Listo!", "Prêt !"),
        "grows_in" to t("Grows in", "Crece en", "Pousse en"),
        "remove" to t("remove", "quitar", "retirer"),

        // Profile screen
        "your_name" to t("Your name", "Tu nombre", "Votre nom"),
        "enter_name" to t("Enter your name", "Ingresa tu nombre", "Entrez votre nom"),
        "favorites" to t("Favorites", "Favoritos", "Favoris"),
        "planted" to t("Planted", "Plantado", "Planté"),
        "favorite_recipes" to t("Favorite Recipes", "Recetas Favoritas", "Recettes Favorites"),
        "garden_stats" to t("My Garden Stats", "Estadísticas del Jardín", "Stats du Jardin"),
        "no_favorites" to t("No favorites yet.\nTap the heart on any recipe!", "Aún no hay favoritos.\n¡Toca el corazón en cualquier receta!", "Pas encore de favoris.\nAppuyez sur le cœur d'une recette !"),
        "no_planted" to t("No fruits planted yet.\nVisit My Garden to start planting!", "Aún no hay frutas plantadas.\n¡Visita Mi Jardín para empezar!", "Pas encore de fruits plantés.\nVisitez Mon Jardin pour commencer !"),

        // Settings
        "theme" to t("Theme", "Tema", "Thème"),
        "language" to t("Language", "Idioma", "Langue"),
        "light" to t("Light", "Claro", "Clair"),
        "light_desc" to t("Clean & bright", "Limpio y brillante", "Propre et lumineux"),
        "dark" to t("Dark", "Oscuro", "Sombre"),
        "dark_desc" to t("Easy on the eyes", "Suave para la vista", "Doux pour les yeux"),
        "fruity" to t("Fruity", "Frutal", "Fruité"),
        "fruity_desc" to t("Animated fruit background", "Fondo animado de frutas", "Arrière-plan fruité animé"),

        // Onboarding
        "onboarding_title_1" to t("Discover Recipes", "Descubre Recetas", "Découvrez des Recettes"),
        "onboarding_desc_1" to t("Explore delicious fruit recipes — from refreshing smoothies and juices to sweet desserts and healthy snacks.", "Explora deliciosas recetas de frutas — desde refrescantes batidos y jugos hasta postres dulces y snacks saludables.", "Explorez de délicieuses recettes de fruits — des smoothies rafraîchissants aux desserts sucrés et collations saines."),
        "onboarding_title_2" to t("Explore Origins", "Explora Orígenes", "Explorez les Origines"),
        "onboarding_desc_2" to t("Find out where your favorite fruits come from. Swipe through countries and discover their unique fruits.", "Descubre de dónde vienen tus frutas favoritas. Desliza por los países y descubre sus frutas únicas.", "Découvrez d'où viennent vos fruits préférés. Parcourez les pays et découvrez leurs fruits uniques."),
        "onboarding_title_3" to t("Grow Your Garden", "Cultiva Tu Jardín", "Cultivez Votre Jardin"),
        "onboarding_desc_3" to t("Plant your own virtual fruit garden. Pick your favorite fruits and watch your collection grow!", "Planta tu propio jardín virtual de frutas. ¡Elige tus frutas favoritas y mira crecer tu colección!", "Plantez votre propre jardin fruitier virtuel. Choisissez vos fruits préférés et regardez votre collection grandir !"),
        "get_started" to t("Get Started", "Comenzar", "Commencer"),
        "next" to t("Next", "Siguiente", "Suivant"),
        "skip" to t("Skip", "Omitir", "Passer"),

        // Months
        "jan" to t("Jan", "Ene", "Jan"),
        "feb" to t("Feb", "Feb", "Fév"),
        "mar" to t("Mar", "Mar", "Mar"),
        "apr" to t("Apr", "Abr", "Avr"),
        "may" to t("May", "May", "Mai"),
        "jun" to t("Jun", "Jun", "Juin"),
        "jul" to t("Jul", "Jul", "Juil"),
        "aug" to t("Aug", "Ago", "Août"),
        "sep" to t("Sep", "Sep", "Sep"),
        "oct" to t("Oct", "Oct", "Oct"),
        "nov" to t("Nov", "Nov", "Nov"),
        "dec" to t("Dec", "Dic", "Déc"),
    )

    fun get(key: String, lang: AppLanguage): String =
        translations[key]?.get(lang) ?: translations[key]?.get(AppLanguage.English) ?: key

    fun monthName(month: Int, lang: AppLanguage): String {
        val keys = listOf("jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec")
        return get(keys[month - 1], lang)
    }
}
