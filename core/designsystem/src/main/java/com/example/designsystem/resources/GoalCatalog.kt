package com.example.designsystem.resources

object GoalCatalog {

    fun forLevel(level: Int): List<ShopItem> {
        val ids = when (level.coerceIn(1, 3)) {
            1 -> listOf("goal_house", "goal_tree")
            2 -> listOf("goal_headphones", "goal_microphone")
            else -> listOf("goal_skateboard", "goal_bike")
        }
        return ids.mapNotNull(ShopItems::findById)
    }
}