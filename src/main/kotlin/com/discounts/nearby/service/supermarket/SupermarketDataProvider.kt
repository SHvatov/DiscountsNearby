package com.discounts.nearby.service.supermarket

import com.discounts.nearby.model.SupermarketCode

/**
 * @author shvatov
 */
interface SupermarketDataProvider {
    /**
     * Code of the supermarket in the system.
     */
    val supermarketCode: SupermarketCode
}