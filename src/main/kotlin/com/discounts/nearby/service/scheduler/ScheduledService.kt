package com.discounts.nearby.service.scheduler

/**
 * @author shvatov
 */
interface ScheduledService {
    /**
     * This method stored the basic business logic that should be
     * run based on some type of the schedule.
     */
    fun runScheduled()
}