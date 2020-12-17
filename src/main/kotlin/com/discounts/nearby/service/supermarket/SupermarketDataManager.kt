package com.discounts.nearby.service.supermarket

/**
 * @author shvatov
 */
interface SupermarketDataManager<S : SupermarketDataProvider> {
    /**
     * Used to set the autowired beans with providers of the data.
     */
    fun setProviders(providers: List<S>)
}