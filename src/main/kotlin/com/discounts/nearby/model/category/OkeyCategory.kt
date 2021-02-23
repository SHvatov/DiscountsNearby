package com.discounts.nearby.model.category

/**
 * @author shvatov
 */
enum class OkeyCategory(
    override val goodCategory: GoodCategory,
    override val localizedCategory: String
) : SupermarketCategory {
    BEER(GoodCategory.BEER, "alkogol-nye-napitki/pivo"),
    MEET(GoodCategory.MEAT, "miaso-ptitsa-kolbasy/miaso-i-ptitsa")
}