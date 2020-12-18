package com.discounts.nearby.service.scheduler.impl

import com.discounts.nearby.config.ComponentsStartupOrder
import com.discounts.nearby.model.Goods
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.scheduler.SupermarketDataUpdateScheduledService
import com.discounts.nearby.service.supermarket.parser.manager.SupermarketSiteDataManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("supermarketDataUpdateScheduledService")
class SupermarketDataUpdateScheduledServiceImpl @Autowired constructor(
    private val supermarketManager: SupermarketSiteDataManager,
    private val supermarketService: SupermarketService
) : SupermarketDataUpdateScheduledService {
    @EventListener(ApplicationReadyEvent::class)
    @Order(ComponentsStartupOrder.SUPERMARKET_DATA_UPDATE)
    override fun updateSupermarketData() {
        runScheduled()
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    override fun runScheduled() {
        supermarketService.findAll().forEach { supermarket ->
            val goodsByPrice = supermarketManager.getAllCategoriesData(
                supermarket.code!!, MAX_GOODS_TO_FETCH, false
            ).values.flatMap { it.goods ?: emptyList() }

            val goodsByDiscount = supermarketManager.getAllCategoriesData(
                supermarket.code!!, MAX_GOODS_TO_FETCH, true
            ).values.flatMap { it.goods ?: emptyList() }

            supermarket.goodsSortedByPrice = Goods(goodsByPrice)
            supermarket.goodsSortedByDiscount = Goods(goodsByDiscount)

            supermarketService.save(supermarket)
        }
    }

    private companion object {
        const val MAX_GOODS_TO_FETCH = 10
    }
}