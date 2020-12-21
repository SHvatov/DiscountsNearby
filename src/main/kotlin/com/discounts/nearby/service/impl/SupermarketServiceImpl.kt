package com.discounts.nearby.service.impl

import com.discounts.nearby.config.ComponentsStartupOrder
import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.repository.SupermarketRepository
import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import java.util.*
import kotlin.streams.toList

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

    override fun getAllCategoriesData(supermarketCode: SupermarketCode,
                                      elementsToFetch: Long,
                                      discountOnly: Boolean): List<Good> {
        val allGoods =
                if (discountOnly)
                    repository.getSupermarketByCode(supermarketCode).goodsSortedByDiscount
                else
                    repository.getSupermarketByCode(supermarketCode).goodsSortedByPrice
        return allGoods!!.goods!!.stream().limit(elementsToFetch).toList()
    }

    override fun getAllDataMapByCategories(supermarketCode: SupermarketCode,
                                           elementsToFetch: Long,
                                           discountOnly: Boolean): Map<GoodCategory, List<Good>> {
        val data = getAllCategoriesData(supermarketCode, elementsToFetch, discountOnly)
        val res: MutableMap<GoodCategory, MutableList<Good>> = EnumMap(GoodCategory::class.java)
        data.forEach {
            if (res.containsKey(it.goodCategory))
                res[it.goodCategory]!!.add(it)
            else
                it.goodCategory?.let { it1 -> res.put(it1, mutableListOf(it)) }
        }
        return res
    }

    override fun getAllCategoriesNames(): List<String> = GoodCategory.values().map { it.toString() }
}