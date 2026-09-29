package com.example.designsystem.resources

import com.example.designsystem.R
import com.example.designsystem.resources.ItemCategory.MANDATORY
import com.example.designsystem.resources.ItemCategory.OPTIONAL
import com.example.designsystem.resources.ItemCategory.GOAL

object ShopItems {

    val items: List<ShopItem> = listOf(
        // MANDATORY
        ShopItem("carrot",     R.string.item_carrot,     R.drawable.carrot_3d,         3,  MANDATORY, effect = 2),
        ShopItem("juice",      R.string.item_juice,      R.drawable.beverage_box_3d,   5,  MANDATORY, effect = 2),
        ShopItem("apple",      R.string.item_apple,      R.drawable.red_apple_3d,      5,  MANDATORY, effect = 3),
        ShopItem("broccoli",   R.string.item_broccoli,   R.drawable.broccoli_3d,       7,  MANDATORY, effect = 4),
        ShopItem("strawberry", R.string.item_strawberry, R.drawable.strawberry_3d,     8,  MANDATORY, effect = 4),
        ShopItem("sandwich",   R.string.item_sandwich,   R.drawable.sandwich_3d,      12,  MANDATORY, effect = 6),

        // OPTIONAL
        ShopItem("candy",      R.string.item_candy,      R.drawable.candy_3d,          3,  OPTIONAL,  effect = 1),
        ShopItem("cake",       R.string.item_cake,       R.drawable.cupcake_3d,        5,  OPTIONAL,  effect = 2),
        ShopItem("teddy",      R.string.item_teddy,      R.drawable.teddy_bear_3d,     7,  OPTIONAL,  effect = 2),
        ShopItem("kite",       R.string.item_kite,       R.drawable.kite_3d,           8,  OPTIONAL,  effect = 3),
        ShopItem("phone",      R.string.item_phone,      R.drawable.mobile_phone_3d,  50,  OPTIONAL,  effect = 5),
        ShopItem("laptop",     R.string.item_laptop,     R.drawable.laptop_3d,        80,  OPTIONAL,  effect = 8),

        // GOAL
        ShopItem("goal_house",       R.string.goal_house,       R.drawable.hut_3d,           80,  GOAL),
        ShopItem("goal_tree",        R.string.goal_tree,        R.drawable.deciduous_tree_3d, 100, GOAL),
        ShopItem("goal_headphones",  R.string.goal_headphones,  R.drawable.headphone_3d,     200, GOAL),
        ShopItem("goal_microphone",  R.string.goal_microphone,  R.drawable.microphone_3d,    250, GOAL),
        ShopItem("goal_skateboard",  R.string.goal_skateboard,  R.drawable.skateboard_3d,    500, GOAL),
        ShopItem("goal_bike",        R.string.goal_bike,        R.drawable.bicycle_3d,       600, GOAL),
    )

    private val byId: Map<String, ShopItem> = items.associateBy { it.id }

    fun findById(id: String?): ShopItem? = id?.let { byId[it] }

    fun requireById(id: String?): ShopItem =
        requireNotNull(findById(id)) { "Unknown shop item id: $id" }

    fun byCategory(category: ItemCategory): List<ShopItem> =
        items.filter { it.category == category }
}