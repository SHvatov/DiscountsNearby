package com.discounts.nearby.service.supermarket.category.provider.impl

import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.model.category.SupermarketCategory
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import java.lang.reflect.ParameterizedType

/**
 * @author shvatov
 */
@Suppress("unchecked_cast")
abstract class AbstractSupermarketCategoryProvider<C : SupermarketCategory> : SupermarketCategoryProvider {
    private val categoryClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<C>

    private val internalCategoryMap = (categoryClass.enumConstants as Array<out Enum<*>>)
        .mapNotNull { it as? SupermarketCategory }
        .associate { it.goodCategory to it.localizedCategory }

    private val outerCategoryMap = (categoryClass.enumConstants as Array<out Enum<*>>)
        .mapNotNull { it as? SupermarketCategory }
        .associate { it.localizedCategory to it.goodCategory }

    override fun getLocalized(category: GoodCategory) =
        internalCategoryMap[category] ?: error("No category found: [$category]")

    override fun getNormalized(category: String) =
        outerCategoryMap[category] ?: error("No category found: [$category]")

    override fun getAllCategories() = internalCategoryMap.keys
}