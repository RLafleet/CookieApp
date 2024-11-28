package com.example.cookieapp

data class ShopItem(
    val name: String,
    val cost: Int,
    val multiplier: Int = 1,
    val passiveRate: Int = 0,
    val maxLevel: Int = 5,
    val currentLevel: Int = 0
) {
    val imageResId: Int
        get() {
            return when (name) {
                "Cursor" -> R.drawable.cursor_64px
                "Grandma" -> R.drawable.grandma_new
                "Farm" -> R.drawable.farm
                "Mine" -> R.drawable.mine_new
                else -> R.drawable.cookie
            }
        }
}

data class ClickerState(
    val cookieCount: Int = 0,
    val clickMultiplier: Int = 1,
    val passiveIncome: Int = 0,
    val shopItems: List<ShopItem> = listOf(
        ShopItem("Double Cookies", 10, multiplier = 2),
        ShopItem("Cookie Generator", 25, passiveRate = 1),
        ShopItem("Advanced Generator", 100, passiveRate = 5),
        ShopItem("Cursor", 5, multiplier = 1),
        ShopItem("Grandma", 20, passiveRate = 2),
        ShopItem("Farm", 50, passiveRate = 5),
        ShopItem("Mine", 150, passiveRate = 10)
    ),
    val elapsedTime: String = "0:00",
    val averageSpeed: Int = 0,
)