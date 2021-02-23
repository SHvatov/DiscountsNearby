package com.discounts.nearby.service.scheduler

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.scheduler.impl.SupermarketDataUpdateScheduledServiceImpl
import com.discounts.nearby.service.supermarket.parser.manager.SupermarketSiteDataManager
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class SupermarketDataUpdateScheduledServiceImplTest {
    @Mock
    private lateinit var supermarketManager: SupermarketSiteDataManager

    @Mock
    private lateinit var supermarketService: SupermarketService

    @InjectMocks
    private lateinit var service: SupermarketDataUpdateScheduledServiceImpl

    @BeforeEach
    fun setUp() {
        whenever(supermarketService.findAll()).thenReturn(SUPERMARKETS)
        whenever(supermarketManager.getAllCategoriesData(eq(SupermarketCode.OKEY), any(), any()))
            .thenReturn(
                mapOf(
                    GoodCategory.BEER to Goods(
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
                )
            )
    }

    @Test
    fun `update data in DB`() {
        service.runScheduled()

        argumentCaptor<Supermarket> {
            verify(supermarketService, times(1)).save(capture())
            assertEquals(SUPERMARKETS[0].id, firstValue.id)
            assertFalse { firstValue.goodsSortedByPrice?.goods.isNullOrEmpty() }
            assertFalse { firstValue.goodsSortedByDiscount?.goods.isNullOrEmpty() }
        }
    }

    private companion object {
        val SUPERMARKETS = listOf(
            Supermarket().apply {
                id = 1L
                code = SupermarketCode.OKEY
                name = "Okey"
            }
        )
    }
}