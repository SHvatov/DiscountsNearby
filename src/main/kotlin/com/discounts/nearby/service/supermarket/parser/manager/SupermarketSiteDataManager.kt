package com.discounts.nearby.service.supermarket.parser.manager

import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.SupermarketDataManager
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider

/**
 * @see SupermarketSiteDataProvider
 * @author shvatov
 */
interface SupermarketSiteDataManager : SupermarketDataManager<SupermarketSiteDataProvider> {
    fun getAllCategoriesData(supermarketCode: SupermarketCode,
                             elementsToFetch: Int,
                             discountOnly: Boolean): Map<GoodCategory, Goods>

    fun getDataByCategory(supermarketCode: SupermarketCode,
                          goodCategory: GoodCategory,
                          elementsToFetch: Int,
                          discountOnly: Boolean): Goods
}