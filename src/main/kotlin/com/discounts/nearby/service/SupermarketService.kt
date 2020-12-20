package com.discounts.nearby.service

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode

/**
 * @author shvatov
 */
interface SupermarketService : CrudService<Supermarket, Long> {
    /**
     * Initializes the data stored in supermarket table with default entities
     * on the start up of the application.
     */
    fun initSupermarkets()

    fun getAllCategoriesData(supermarketCode: SupermarketCode,
                             elementsToFetch: Long,
                             discountOnly: Boolean): List<Good>
}