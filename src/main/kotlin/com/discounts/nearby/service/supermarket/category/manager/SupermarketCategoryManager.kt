package com.discounts.nearby.service.supermarket.category.manager

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.SupermarketDataManager
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider

/**
 * @see [SupermarketCategoryProvider]
 * @author shvatov
 */
interface SupermarketCategoryManager : SupermarketDataManager<SupermarketCategoryProvider> {
    fun getLocalized(supermarketCode: SupermarketCode,
                     goodCategory: GoodCategory): String

    fun getNormalized(supermarketCode: SupermarketCode,
                      category: String): GoodCategory

    fun getAllCategories(supermarketCode: SupermarketCode): Set<GoodCategory>
}