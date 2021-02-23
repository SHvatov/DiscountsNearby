package com.discounts.nearby.model.category

/**
 * @author shvatov
 */
enum class LentaCategory(
    override val goodCategory: GoodCategory,
    override val localizedCategory: String
) : SupermarketCategory {
    BEER(GoodCategory.BEER, "alkogolnye-napitki"),
    MEAT(GoodCategory.MEAT, "myaso-ptica-kolbasa")
}