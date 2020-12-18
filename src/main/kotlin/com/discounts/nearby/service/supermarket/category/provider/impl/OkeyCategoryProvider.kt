package com.discounts.nearby.service.supermarket.category.provider.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.OkeyCategory
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import org.springframework.stereotype.Component

/**
 * @author shvatov
 */
@Component("okeyCategoryProvider")
class OkeyCategoryProvider :
    AbstractSupermarketCategoryProvider<OkeyCategory>(),
    SupermarketCategoryProvider {
    override val supermarketCode = SupermarketCode.OKEY
}
