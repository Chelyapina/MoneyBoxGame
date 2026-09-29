package com.example.designsystem.resources

data class ShopItem(
    val id: String,
    val nameRes: Int,
    val iconRes: Int,
    val price: Int,
    val category: ItemCategory,
    val effect: Int = 0,
)

enum class ItemCategory {
    MANDATORY,
    OPTIONAL,
    GOAL
}