package com.discounts.nearby.service.supermarket.category.manager.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

/**
 * @author Created by Vladislav Marchenko on 03.03.2021
 */
@ExtendWith(MockitoExtension::class)
internal class SupermarketCategoryManagerImplTest {

    @Mock
    lateinit var lentaCategoryProvider: SupermarketCategoryProvider

    @Mock
    lateinit var okeyCategoryProvider: SupermarketCategoryProvider

    @InjectMocks
    lateinit var supermarketCategoryManager: SupermarketCategoryManagerImpl

    @BeforeEach
    fun setUp() {
        whenever(lentaCategoryProvider.supermarketCode).thenReturn(SupermarketCode.LENTA)
        whenever(okeyCategoryProvider.supermarketCode).thenReturn(SupermarketCode.OKEY)
        supermarketCategoryManager.setProviders(listOf(lentaCategoryProvider, okeyCategoryProvider))
    }

    @Test
    fun `test get localized method`() {
        supermarketCategoryManager.getLocalized(SupermarketCode.LENTA, GoodCategory.MEAT)
        verify(lentaCategoryProvider, times(1)).getLocalized(GoodCategory.MEAT)
    }

    @Test
    fun getNormalized() {
    }

    @Test
    fun getAllCategories() {
    }
}