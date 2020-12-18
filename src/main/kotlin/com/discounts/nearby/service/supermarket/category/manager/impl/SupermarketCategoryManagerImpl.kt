package com.discounts.nearby.service.supermarket.category.manager.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("supermarketCategoryManager")
class SupermarketCategoryManagerImpl : SupermarketCategoryManager {
    private lateinit var categoryProviders: Map<SupermarketCode, SupermarketCategoryProvider>

    @Autowired
    override fun setProviders(providers: List<SupermarketCategoryProvider>) {
        categoryProviders = providers.associateBy { it.supermarketCode }
    }

    override fun getLocalized(supermarketCode: SupermarketCode,
                              goodCategory: GoodCategory) =
        getProvider(supermarketCode).getLocalized(goodCategory)

    override fun getNormalized(supermarketCode: SupermarketCode, category: String) =
        getProvider(supermarketCode).getNormalized(category)

    override fun getAllCategories(supermarketCode: SupermarketCode) =
        getProvider(supermarketCode).getAllCategories()

    private fun getProvider(supermarketCode: SupermarketCode) =
        categoryProviders[supermarketCode] ?: error("No provider for $supermarketCode found")
}