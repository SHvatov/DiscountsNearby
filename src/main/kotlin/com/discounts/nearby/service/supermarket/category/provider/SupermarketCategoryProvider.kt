package com.discounts.nearby.service.supermarket.category.provider

import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.SupermarketDataProvider

/**
 * @author shvatov
 */
interface SupermarketCategoryProvider : SupermarketDataProvider {
    /**
     * Converts the provided [category] into a localized string,
     * which can be then used in order to fetch the data from the web-site of
     * the supermarket.
     */
    fun getLocalized(category: GoodCategory): String

    /**
     * Converts the provided [category] into an instance of [GoodCategory].
     */
    fun getNormalized(category: String): GoodCategory

    /**
     * Returns all supported categories of goods.
     */
    fun getAllCategories(): Set<GoodCategory>
}