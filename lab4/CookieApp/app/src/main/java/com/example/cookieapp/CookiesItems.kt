package com.example.cookieapp

data class ShopItem(
    val name: String,
    val cost: Int,
    val multiplier: Int = 1,
    val passiveRate: Int = 0,
    val maxLevel: Int = 5,
    val currentLevel: Int = 0
)

// добавить доступность по условиям
data class ClickerState(
    val cookieCount: Int = 0,
    val clickMultiplier: Int = 1,
    val passiveIncome: Int = 0,
    val shopItems: List<ShopItem> = listOf(
        ShopItem("Double Cookies", 10, multiplier = 2),
        ShopItem("Cookie Generator", 25, passiveRate = 1),
        ShopItem("Advanced Generator", 100, passiveRate = 5)
    ),
    val elapsedTime: String = "00:00:00",
    val averageSpeed: Int = 0,
)