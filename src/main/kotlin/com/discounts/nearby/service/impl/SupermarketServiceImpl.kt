package com.discounts.nearby.service.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.repository.SupermarketRepository
import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.streams.toList

/**
 * @author shvatov
 */
@Service("supermarketService")
class SupermarketServiceImpl @Autowired constructor(
        repository: SupermarketRepository
) : AbstractCrudService<SupermarketRepository, Supermarket, Long>(repository), SupermarketService {
    fun getAllCategoriesData(supermarketCode: SupermarketCode,
                             elementsToFetch: Long,
                             discountOnly: Boolean): List<Good> {
        val allGoods =
                if (discountOnly)
                    repository.getSupermarketByCode(supermarketCode).goodsSortedByDiscount
                else
                    repository.getSupermarketByCode(supermarketCode).goodsSortedByPrice
        return allGoods!!.goods!!.stream().limit(elementsToFetch).toList()
    }
}