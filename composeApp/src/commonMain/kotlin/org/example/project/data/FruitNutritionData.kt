package org.example.project.data

object FruitNutritionData {

    private val fruits = listOf(
        FruitInfo("Apple", "\uD83C\uDF4E", "Rosaceae", "Central Asia", listOf("China", "USA", "Turkey"), "Temperate", 52, 0.3, 13.8, 2.4, 10.4, 0.2, 4.6, 3.0, 107, 6, 0.1, 85.6, "An apple tree can produce fruit for over 100 years."),
        FruitInfo("Banana", "\uD83C\uDF4C", "Musaceae", "Southeast Asia", listOf("India", "Ecuador", "Philippines"), "Tropical", 89, 1.1, 22.8, 2.6, 12.2, 0.3, 8.7, 3.0, 358, 5, 0.3, 74.9, "Bananas are technically berries, while strawberries are not."),
        FruitInfo("Orange", "\uD83C\uDF4A", "Rutaceae", "Southeast Asia", listOf("Brazil", "China", "India"), "Subtropical", 47, 0.9, 11.8, 2.4, 9.4, 0.1, 53.2, 11.0, 181, 40, 0.1, 86.8, "A single orange tree can produce 60,000 oranges in its lifetime."),
        FruitInfo("Strawberry", "\uD83C\uDF53", "Rosaceae", "Europe", listOf("China", "USA", "Mexico"), "Temperate", 32, 0.7, 7.7, 2.0, 4.9, 0.3, 58.8, 1.0, 153, 16, 0.4, 91.0, "Strawberries have their seeds on the outside — about 200 per berry."),
        FruitInfo("Grape", "\uD83C\uDF47", "Vitaceae", "Near East", listOf("China", "Italy", "USA"), "Temperate", 69, 0.7, 18.1, 0.9, 15.5, 0.2, 3.2, 3.0, 191, 10, 0.4, 80.5, "It takes about 2.5 pounds of grapes to make one bottle of wine."),
        FruitInfo("Watermelon", "\uD83C\uDF49", "Cucurbitaceae", "West Africa", listOf("China", "Turkey", "Iran"), "Tropical", 30, 0.6, 7.6, 0.4, 6.2, 0.2, 8.1, 28.0, 112, 7, 0.2, 91.4, "Watermelon is 92% water, making it one of the most hydrating fruits."),
        FruitInfo("Mango", "\uD83E\uDD6D", "Anacardiaceae", "South Asia", listOf("India", "China", "Thailand"), "Tropical", 60, 0.8, 15.0, 1.6, 13.7, 0.4, 36.4, 54.0, 168, 11, 0.2, 83.5, "Mango is the national fruit of India, Pakistan, and the Philippines."),
        FruitInfo("Pineapple", "\uD83C\uDF4D", "Bromeliaceae", "South America", listOf("Costa Rica", "Philippines", "Brazil"), "Tropical", 50, 0.5, 13.1, 1.4, 9.9, 0.1, 47.8, 3.0, 109, 13, 0.3, 86.0, "A pineapple takes about 2-3 years to grow from planting to harvest."),
        FruitInfo("Cherry", "\uD83C\uDF52", "Rosaceae", "Europe / West Asia", listOf("Turkey", "USA", "Chile"), "Temperate", 50, 1.0, 12.2, 1.6, 8.5, 0.3, 7.0, 3.0, 173, 13, 0.4, 82.3, "There are over 1,000 varieties of cherries, but only about 10 are commercially grown."),
        FruitInfo("Lemon", "\uD83C\uDF4B", "Rutaceae", "Northeast India", listOf("India", "Mexico", "China"), "Subtropical", 29, 1.1, 9.3, 2.8, 2.5, 0.3, 53.0, 1.0, 138, 26, 0.6, 89.0, "Lemons contain more sugar than strawberries — the sourness masks it."),
        FruitInfo("Kiwi", "\uD83E\uDD5D", "Actinidiaceae", "China", listOf("China", "Italy", "New Zealand"), "Subtropical", 61, 1.1, 14.7, 3.0, 9.0, 0.5, 92.7, 4.0, 312, 34, 0.3, 83.1, "Kiwi has more vitamin C than an orange."),
        FruitInfo("Peach", "\uD83C\uDF51", "Rosaceae", "Northwest China", listOf("China", "Spain", "Italy"), "Temperate", 39, 0.9, 9.5, 1.5, 8.4, 0.3, 6.6, 16.0, 190, 6, 0.3, 89.0, "In China, the peach is a symbol of immortality and longevity."),
        FruitInfo("Pear", "\uD83C\uDF50", "Rosaceae", "Europe / West Asia", listOf("China", "USA", "Argentina"), "Temperate", 57, 0.4, 15.2, 3.1, 9.8, 0.1, 4.3, 1.0, 116, 9, 0.2, 84.0, "Pears ripen from the inside out — they're picked unripe on purpose."),
        FruitInfo("Plum", "\uD83E\uDD6D", "Rosaceae", "China", listOf("China", "Romania", "Serbia"), "Temperate", 46, 0.7, 11.4, 1.4, 9.9, 0.3, 9.5, 17.0, 157, 6, 0.2, 87.2, "Dried plums (prunes) have been shown to help build stronger bones."),
        FruitInfo("Blueberry", "\uD83E\uDED0", "Ericaceae", "North America", listOf("USA", "Canada", "Chile"), "Temperate", 57, 0.7, 14.5, 2.4, 10.0, 0.3, 9.7, 3.0, 77, 6, 0.3, 84.2, "Blueberries are one of the only naturally blue foods in the world."),
        FruitInfo("Raspberry", "\uD83E\uDD6D", "Rosaceae", "Europe", listOf("Russia", "Poland", "USA"), "Temperate", 52, 1.2, 11.9, 6.5, 4.4, 0.7, 26.2, 2.0, 151, 25, 0.7, 85.8, "A single raspberry is made up of about 100 tiny individual fruits called drupelets."),
        FruitInfo("Papaya", "\uD83E\uDD6D", "Caricaceae", "Central America", listOf("India", "Brazil", "Indonesia"), "Tropical", 43, 0.5, 10.8, 1.7, 7.8, 0.3, 60.9, 47.0, 182, 20, 0.3, 88.1, "Papaya was called 'the fruit of the angels' by Christopher Columbus."),
        FruitInfo("Coconut", "\uD83E\uDD65", "Arecaceae", "Southeast Asia", listOf("Indonesia", "Philippines", "India"), "Tropical", 354, 3.3, 15.2, 9.0, 6.2, 33.5, 3.3, 0.0, 356, 14, 2.4, 47.0, "A coconut can travel thousands of miles by sea and still germinate."),
        FruitInfo("Pomegranate", "\uD83E\uDD6D", "Lythraceae", "Iran", listOf("India", "China", "Iran"), "Subtropical", 83, 1.7, 18.7, 4.0, 13.7, 1.2, 10.2, 0.0, 236, 10, 0.3, 77.9, "A single pomegranate can contain up to 1,400 seeds."),
        FruitInfo("Fig", "\uD83E\uDED2", "Moraceae", "Western Asia", listOf("Turkey", "Egypt", "Morocco"), "Subtropical", 74, 0.8, 19.2, 2.9, 16.3, 0.3, 2.0, 7.0, 232, 35, 0.4, 79.1, "Figs are one of the oldest cultivated fruits — over 11,000 years."),
        FruitInfo("Avocado", "\uD83E\uDD51", "Lauraceae", "Mexico", listOf("Mexico", "Dominican Rep.", "Peru"), "Subtropical", 160, 2.0, 8.5, 6.7, 0.7, 14.7, 10.0, 7.0, 485, 12, 0.6, 73.2, "Avocados don't ripen on the tree — they only ripen after being picked."),
        FruitInfo("Passion Fruit", "\uD83D\uDC9B", "Passifloraceae", "South America", listOf("Brazil", "Indonesia", "Colombia"), "Tropical", 97, 2.2, 23.4, 10.4, 11.2, 0.7, 30.0, 64.0, 348, 12, 1.6, 72.9, "Passion fruit got its name from missionaries who saw its flower as a symbol of the Passion of Christ."),
        FruitInfo("Guava", "\uD83C\uDF4F", "Myrtaceae", "Central America", listOf("India", "China", "Thailand"), "Tropical", 68, 2.6, 14.3, 5.4, 8.9, 1.0, 228.3, 31.0, 417, 18, 0.3, 80.8, "Guava has 4x more vitamin C than an orange."),
        FruitInfo("Lychee", "\uD83E\uDD6D", "Sapindaceae", "Southern China", listOf("China", "India", "Thailand"), "Subtropical", 66, 0.8, 16.5, 1.3, 15.2, 0.4, 71.5, 0.0, 171, 5, 0.3, 81.8, "Lychee trees can live for over 1,000 years."),
        FruitInfo("Dragon Fruit", "\uD83D\uDC9C", "Cactaceae", "Central America", listOf("Vietnam", "China", "Indonesia"), "Tropical", 50, 1.1, 11.0, 3.0, 8.0, 0.4, 3.0, 0.0, 227, 8, 0.7, 87.0, "Dragon fruit grows on a climbing cactus that blooms only at night."),
        FruitInfo("Mandarin", "\uD83C\uDF4A", "Rutaceae", "China", listOf("China", "Spain", "Turkey"), "Subtropical", 53, 0.8, 13.3, 1.8, 10.6, 0.3, 26.7, 34.0, 166, 37, 0.2, 85.2, "Mandarins are the most widely grown citrus fruit worldwide."),
        FruitInfo("Melon", "\uD83C\uDF48", "Cucurbitaceae", "Persia", listOf("China", "Turkey", "Iran"), "Warm", 34, 0.8, 8.2, 0.9, 7.9, 0.2, 36.7, 169.0, 267, 9, 0.2, 90.2, "Melons are related to cucumbers and squash."),
        FruitInfo("Jackfruit", "\uD83E\uDD6D", "Moraceae", "India", listOf("India", "Bangladesh", "Thailand"), "Tropical", 95, 1.7, 23.3, 1.5, 19.1, 0.6, 13.7, 5.0, 448, 24, 0.2, 73.5, "Jackfruit is the largest tree-borne fruit — up to 55 kg each."),
        FruitInfo("Durian", "\uD83E\uDD6D", "Malvaceae", "Borneo / Sumatra", listOf("Thailand", "Malaysia", "Indonesia"), "Tropical", 147, 1.5, 27.1, 3.8, 0.0, 5.3, 19.7, 2.0, 436, 6, 0.4, 65.0, "Durian is banned in many hotels due to its extremely strong smell."),
        FruitInfo("Cranberry", "\uD83E\uDD6D", "Ericaceae", "North America", listOf("USA", "Canada", "Chile"), "Temperate", 46, 0.4, 12.2, 4.6, 4.0, 0.1, 13.3, 3.0, 85, 8, 0.3, 87.1, "Cranberries bounce when ripe — farmers use this to test quality."),
    )

    private val fruitMap = fruits.associateBy { it.name.lowercase() }

    fun getByName(name: String): FruitInfo? = fruitMap[name.lowercase()]

    val allFruitNames: List<String> = fruits.map { it.name }.sorted()

    val allFruits: List<FruitInfo> = fruits
}
