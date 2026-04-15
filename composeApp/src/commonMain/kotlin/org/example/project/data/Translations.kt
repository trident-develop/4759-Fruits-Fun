package org.example.project.data

import org.example.project.theme.AppLanguage

object Translations {
    // Country names
    private val countries = mapOf(
        "Spain" to mapOf(AppLanguage.Spanish to "España", AppLanguage.French to "Espagne"),
        "Italy" to mapOf(AppLanguage.Spanish to "Italia", AppLanguage.French to "Italie"),
        "Brazil" to mapOf(AppLanguage.Spanish to "Brasil", AppLanguage.French to "Brésil"),
        "India" to mapOf(AppLanguage.Spanish to "India", AppLanguage.French to "Inde"),
        "Thailand" to mapOf(AppLanguage.Spanish to "Tailandia", AppLanguage.French to "Thaïlande"),
        "Mexico" to mapOf(AppLanguage.Spanish to "México", AppLanguage.French to "Mexique"),
        "Greece" to mapOf(AppLanguage.Spanish to "Grecia", AppLanguage.French to "Grèce"),
        "Turkey" to mapOf(AppLanguage.Spanish to "Turquía", AppLanguage.French to "Turquie"),
        "Vietnam" to mapOf(AppLanguage.Spanish to "Vietnam", AppLanguage.French to "Viêt Nam"),
        "Indonesia" to mapOf(AppLanguage.Spanish to "Indonesia", AppLanguage.French to "Indonésie"),
        "Japan" to mapOf(AppLanguage.Spanish to "Japón", AppLanguage.French to "Japon"),
        "China" to mapOf(AppLanguage.Spanish to "China", AppLanguage.French to "Chine"),
        "Australia" to mapOf(AppLanguage.Spanish to "Australia", AppLanguage.French to "Australie"),
        "Egypt" to mapOf(AppLanguage.Spanish to "Egipto", AppLanguage.French to "Égypte"),
        "Morocco" to mapOf(AppLanguage.Spanish to "Marruecos", AppLanguage.French to "Maroc"),
        "South Africa" to mapOf(AppLanguage.Spanish to "Sudáfrica", AppLanguage.French to "Afrique du Sud"),
        "Argentina" to mapOf(AppLanguage.Spanish to "Argentina", AppLanguage.French to "Argentine"),
        "Chile" to mapOf(AppLanguage.Spanish to "Chile", AppLanguage.French to "Chili"),
        "USA" to mapOf(AppLanguage.Spanish to "EE.UU.", AppLanguage.French to "États-Unis"),
        "France" to mapOf(AppLanguage.Spanish to "Francia", AppLanguage.French to "France"),
    )

    // Fruit names
    private val fruits = mapOf(
        "Apple" to mapOf(AppLanguage.Spanish to "Manzana", AppLanguage.French to "Pomme"),
        "Banana" to mapOf(AppLanguage.Spanish to "Plátano", AppLanguage.French to "Banane"),
        "Orange" to mapOf(AppLanguage.Spanish to "Naranja", AppLanguage.French to "Orange"),
        "Mango" to mapOf(AppLanguage.Spanish to "Mango", AppLanguage.French to "Mangue"),
        "Kiwi" to mapOf(AppLanguage.Spanish to "Kiwi", AppLanguage.French to "Kiwi"),
        "Strawberry" to mapOf(AppLanguage.Spanish to "Fresa", AppLanguage.French to "Fraise"),
        "Pineapple" to mapOf(AppLanguage.Spanish to "Piña", AppLanguage.French to "Ananas"),
        "Peach" to mapOf(AppLanguage.Spanish to "Melocotón", AppLanguage.French to "Pêche"),
        "Pear" to mapOf(AppLanguage.Spanish to "Pera", AppLanguage.French to "Poire"),
        "Watermelon" to mapOf(AppLanguage.Spanish to "Sandía", AppLanguage.French to "Pastèque"),
        "Cherry" to mapOf(AppLanguage.Spanish to "Cereza", AppLanguage.French to "Cerise"),
        "Grape" to mapOf(AppLanguage.Spanish to "Uva", AppLanguage.French to "Raisin"),
        "Lemon" to mapOf(AppLanguage.Spanish to "Limón", AppLanguage.French to "Citron"),
        "Lime" to mapOf(AppLanguage.Spanish to "Lima", AppLanguage.French to "Citron vert"),
        "Coconut" to mapOf(AppLanguage.Spanish to "Coco", AppLanguage.French to "Noix de coco"),
        "Avocado" to mapOf(AppLanguage.Spanish to "Aguacate", AppLanguage.French to "Avocat"),
        "Blueberry" to mapOf(AppLanguage.Spanish to "Arándano", AppLanguage.French to "Myrtille"),
        "Raspberry" to mapOf(AppLanguage.Spanish to "Frambuesa", AppLanguage.French to "Framboise"),
        "Fig" to mapOf(AppLanguage.Spanish to "Higo", AppLanguage.French to "Figue"),
        "Plum" to mapOf(AppLanguage.Spanish to "Ciruela", AppLanguage.French to "Prune"),
        "Pomegranate" to mapOf(AppLanguage.Spanish to "Granada", AppLanguage.French to "Grenade"),
        "Papaya" to mapOf(AppLanguage.Spanish to "Papaya", AppLanguage.French to "Papaye"),
        "Guava" to mapOf(AppLanguage.Spanish to "Guayaba", AppLanguage.French to "Goyave"),
        "Passion Fruit" to mapOf(AppLanguage.Spanish to "Maracuyá", AppLanguage.French to "Fruit de la passion"),
        "Dragon Fruit" to mapOf(AppLanguage.Spanish to "Pitahaya", AppLanguage.French to "Fruit du dragon"),
        "Lychee" to mapOf(AppLanguage.Spanish to "Lichi", AppLanguage.French to "Litchi"),
        "Mandarin" to mapOf(AppLanguage.Spanish to "Mandarina", AppLanguage.French to "Mandarine"),
        "Melon" to mapOf(AppLanguage.Spanish to "Melón", AppLanguage.French to "Melon"),
        "Nectarine" to mapOf(AppLanguage.Spanish to "Nectarina", AppLanguage.French to "Nectarine"),
        "Apricot" to mapOf(AppLanguage.Spanish to "Albaricoque", AppLanguage.French to "Abricot"),
        "Grapefruit" to mapOf(AppLanguage.Spanish to "Pomelo", AppLanguage.French to "Pamplemousse"),
        "Blackberry" to mapOf(AppLanguage.Spanish to "Mora", AppLanguage.French to "Mûre"),
        "Cranberry" to mapOf(AppLanguage.Spanish to "Arándano rojo", AppLanguage.French to "Canneberge"),
        "Jackfruit" to mapOf(AppLanguage.Spanish to "Jaca", AppLanguage.French to "Jacquier"),
        "Durian" to mapOf(AppLanguage.Spanish to "Durián", AppLanguage.French to "Durian"),
        "Rambutan" to mapOf(AppLanguage.Spanish to "Rambután", AppLanguage.French to "Ramboutan"),
        "Mangosteen" to mapOf(AppLanguage.Spanish to "Mangostán", AppLanguage.French to "Mangoustan"),
        "Longan" to mapOf(AppLanguage.Spanish to "Longán", AppLanguage.French to "Longanier"),
        "Persimmon" to mapOf(AppLanguage.Spanish to "Caqui", AppLanguage.French to "Kaki"),
        "Date" to mapOf(AppLanguage.Spanish to "Dátil", AppLanguage.French to "Datte"),
        "Quince" to mapOf(AppLanguage.Spanish to "Membrillo", AppLanguage.French to "Coing"),
        "Ginger" to mapOf(AppLanguage.Spanish to "Jengibre", AppLanguage.French to "Gingembre"),
        "Mint" to mapOf(AppLanguage.Spanish to "Menta", AppLanguage.French to "Menthe"),
        "Carrot" to mapOf(AppLanguage.Spanish to "Zanahoria", AppLanguage.French to "Carotte"),
        "Cantaloupe" to mapOf(AppLanguage.Spanish to "Cantalupo", AppLanguage.French to "Cantaloup"),
        "Honeydew" to mapOf(AppLanguage.Spanish to "Melón verde", AppLanguage.French to "Melon miel"),
        "Pomelo" to mapOf(AppLanguage.Spanish to "Pomelo", AppLanguage.French to "Pomélo"),
        "Acai" to mapOf(AppLanguage.Spanish to "Açaí", AppLanguage.French to "Açaï"),
    )

    // Category names
    private val categories = mapOf(
        "Smoothies" to mapOf(AppLanguage.Spanish to "Batidos", AppLanguage.French to "Smoothies"),
        "Juices" to mapOf(AppLanguage.Spanish to "Jugos", AppLanguage.French to "Jus"),
        "Fruit Salads" to mapOf(AppLanguage.Spanish to "Ensaladas de Frutas", AppLanguage.French to "Salades de Fruits"),
        "Fruit Desserts" to mapOf(AppLanguage.Spanish to "Postres de Frutas", AppLanguage.French to "Desserts aux Fruits"),
        "Fruit Bowls" to mapOf(AppLanguage.Spanish to "Bowls de Frutas", AppLanguage.French to "Bols de Fruits"),
        "Breakfast with Fruits" to mapOf(AppLanguage.Spanish to "Desayuno con Frutas", AppLanguage.French to "Petit-déjeuner aux Fruits"),
        "Frozen Treats" to mapOf(AppLanguage.Spanish to "Helados y Sorbetes", AppLanguage.French to "Desserts Glacés"),
        "Tropical Mixes" to mapOf(AppLanguage.Spanish to "Mezclas Tropicales", AppLanguage.French to "Mélanges Tropicaux"),
        "Healthy Snacks" to mapOf(AppLanguage.Spanish to "Snacks Saludables", AppLanguage.French to "Snacks Sains"),
        "Kids Fruit Recipes" to mapOf(AppLanguage.Spanish to "Recetas para Niños", AppLanguage.French to "Recettes pour Enfants"),
        "Fruit Drinks" to mapOf(AppLanguage.Spanish to "Bebidas de Frutas", AppLanguage.French to "Boissons aux Fruits"),
        "Fruit Jams & Spreads" to mapOf(AppLanguage.Spanish to "Mermeladas y Untables", AppLanguage.French to "Confitures et Tartinades"),
        "Fruit Cakes & Bakes" to mapOf(AppLanguage.Spanish to "Pasteles y Hornados", AppLanguage.French to "Gâteaux et Pâtisseries"),
        "Fruit Sauces & Toppings" to mapOf(AppLanguage.Spanish to "Salsas y Toppings", AppLanguage.French to "Sauces et Garnitures"),
        "Fruit Salad Dressings" to mapOf(AppLanguage.Spanish to "Aderezos para Ensaladas", AppLanguage.French to "Vinaigrettes pour Salades"),
    )

    fun country(name: String, lang: AppLanguage): String =
        if (lang == AppLanguage.English) name else countries[name]?.get(lang) ?: name

    fun fruit(name: String, lang: AppLanguage): String =
        if (lang == AppLanguage.English) name else fruits[name]?.get(lang) ?: name

    fun category(name: String, lang: AppLanguage): String =
        if (lang == AppLanguage.English) name else categories[name]?.get(lang) ?: name
}
