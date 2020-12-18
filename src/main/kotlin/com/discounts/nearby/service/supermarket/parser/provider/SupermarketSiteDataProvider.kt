package com.discounts.nearby.service.supermarket.parser.provider

import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.SupermarketDataProvider

/**
 * @author shvatov
 */
interface SupermarketSiteDataProvider : SupermarketDataProvider {
    /**
     * Finds the [elementsToFetch] products for each category registered in the system.
     * [discountOnly] flag determines, whether information about products with discount only should be fetched
     * from the site or not.
     */
    fun getAllCategoriesData(elementsToFetch: Int, discountOnly: Boolean): Map<GoodCategory, Goods>

    /**
     * Finds goods only by specific [goodCategory].
     * @see getAllCategoriesData
     */
    fun getDataByCategory(goodCategory: GoodCategory, elementsToFetch: Int, discountOnly: Boolean): Goods
}