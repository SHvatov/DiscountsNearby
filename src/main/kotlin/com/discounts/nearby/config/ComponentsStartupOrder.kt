package com.discounts.nearby.config

/**
 * @author shvatov
 */
object ComponentsStartupOrder {
    /**
     * Supermarket entities are created on the init of the app.
     */
    const val SUPERMARKET_CREATION = 1

    /**
     * Supermarket entities are populated with goods data
     * on the init of the app
     */
    const val SUPERMARKET_DATA_UPDATE = 100
}