package com.discounts.nearby.service.supermarket.category.provider.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.LentaCategory
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("lentaCategoryProvider")
class LentaCategoryProvider :
    AbstractSupermarketCategoryProvider<LentaCategory>(),
    SupermarketCategoryProvider {
    override val supermarketCode = SupermarketCode.OKEY
}