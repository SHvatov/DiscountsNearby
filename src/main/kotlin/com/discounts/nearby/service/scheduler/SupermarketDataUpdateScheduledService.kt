package com.discounts.nearby.service.scheduler

/**
 * @author shvatov
 */
interface SupermarketDataUpdateScheduledService : ScheduledService {
    /**
     * Updates the data stored in the database on the init of the application.
     */
    fun updateSupermarketData()
}