package com.discounts.nearby.service.supermarket.parser.manager.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.parser.manager.SupermarketSiteDataManager
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("supermarketSiteDataManager")
class SupermarketSiteDataManagerImpl : SupermarketSiteDataManager {
    private lateinit var dataProviders: Map<SupermarketCode, SupermarketSiteDataProvider>

    override fun setProviders(providers: List<SupermarketSiteDataProvider>) {
        dataProviders = providers.associateBy { it.supermarketCode }
    }

    override fun getAllCategoriesData(supermarketCode: SupermarketCode,
                                      elementsToFetch: Int,
                                      discountOnly: Boolean) =
        getProvider(supermarketCode).getAllCategoriesData(elementsToFetch, discountOnly)

    override fun getDataByCategory(supermarketCode: SupermarketCode,
                                   goodCategory: GoodCategory,
                                   elementsToFetch: Int,
                                   discountOnly: Boolean) =
        getProvider(supermarketCode).getDataByCategory(goodCategory, elementsToFetch, discountOnly)

    private fun getProvider(supermarketCode: SupermarketCode) =
        dataProviders[supermarketCode] ?: error("No provider for $supermarketCode found")
}