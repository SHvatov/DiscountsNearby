package com.discounts.nearby.service.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.repository.SupermarketRepository
import com.discounts.nearby.service.SupermarketService
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

/**
 * @author shvatov
 */
@ExtendWith(MockitoExtension::class)
internal class SupermarketServiceImplTest {
    private lateinit var repository: SupermarketRepository

    private lateinit var service: SupermarketService

    @Test
    fun `init supermarkets info`() {
        prepareService {
            onGeneric { saveAll(any<List<Supermarket>>()) }.thenAnswer {
                @Suppress("unchecked_cast")
                (it.arguments[0] as List<Supermarket>).also { list ->
                    assertEquals(2, list.size)
                    assertTrue { list.any { it.code == SupermarketCode.OKEY } }
                    assertTrue { list.any { it.code == SupermarketCode.LENTA } }
                }
            }
        }

        service = SupermarketServiceImpl(repository)
        service.initSupermarkets()
    }

    @Test
    fun `get categories - sorted by discount`() {
        prepareService {
            on { getSupermarketByCode(eq(SupermarketCode.OKEY)) }.thenReturn(SUPERMARKET_DATA)
        }

        val data = service.getAllCategoriesData(SupermarketCode.OKEY, 10, true)
        assertEquals(1, data.size)
        assertEquals(SUPERMARKET_DATA.goodsSortedByDiscount!!.goods!![0], data[0])
    }

    @Test
    fun `get categories - sorted by price`() {
        prepareService {
            on { getSupermarketByCode(eq(SupermarketCode.OKEY)) }.thenReturn(SUPERMARKET_DATA)
        }

        val data = service.getAllCategoriesData(SupermarketCode.OKEY, 10, false)
        assertEquals(1, data.size)
        assertEquals(SUPERMARKET_DATA.goodsSortedByPrice!!.goods!![0], data[0])
    }

    private fun prepareService(setupRepo: KStubbing<SupermarketRepository>.() -> Unit) {
        repository = mock { setupRepo() }
        service = SupermarketServiceImpl(repository)
    }

    private companion object {
        val SUPERMARKET_DATA = Supermarket().apply {
            id = 1L
            code = SupermarketCode.OKEY
            name = "Okey"
            goodsSortedByDiscount = Goods(
                listOf(
                    Good(
                        name = "Beer",
                        goodCategory = GoodCategory.BEER,
                        weight = "1kg",
                        discount = BigDecimal.TEN,
                        price = BigDecimal.ONE
                    )
                )
            )
            goodsSortedByPrice = Goods(
                listOf(
                    Good(
                        name = "Meat",
                        goodCategory = GoodCategory.MEAT,
                        weight = "1kg",
                        discount = BigDecimal.ONE,
                        price = BigDecimal.TEN
                    )
                )
            )
        }
    }
}