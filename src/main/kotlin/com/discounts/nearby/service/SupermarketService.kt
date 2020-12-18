package com.discounts.nearby.service

import com.discounts.nearby.model.Supermarket

/**
 * @author shvatov
 */
interface SupermarketService : CrudService<Supermarket, Long> {
    /**
     * Initializes the data stored in supermarket table with default entities
     * on the start up of the application.
     */
    fun initSupermarkets()
}