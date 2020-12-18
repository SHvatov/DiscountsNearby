package com.discounts.nearby.service.impl

import com.discounts.nearby.config.ComponentsStartupOrder
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.repository.SupermarketRepository
import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("supermarketService")
class SupermarketServiceImpl @Autowired constructor(
    repository: SupermarketRepository
) : AbstractCrudService<SupermarketRepository, Supermarket, Long>(repository), SupermarketService {
    @EventListener(ApplicationReadyEvent::class)
    @Order(ComponentsStartupOrder.SUPERMARKET_CREATION)
    override fun initSupermarkets() {
        repository.saveAll(
            listOf(
                Supermarket().apply {
                    name = "Лента"
                    code = SupermarketCode.LENTA
                },
                Supermarket().apply {
                    name = "Okey"
                    code = SupermarketCode.OKEY
                }
            )
        )
    }
}