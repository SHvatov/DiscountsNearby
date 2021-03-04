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
    fun `test getLocalized() method`() {
        supermarketCategoryManager.getLocalized(SupermarketCode.LENTA, GoodCategory.MEAT)
        verify(lentaCategoryProvider, times(1)).getLocalized(GoodCategory.MEAT)

        supermarketCategoryManager.getLocalized(SupermarketCode.OKEY, GoodCategory.MEAT)
        verify(okeyCategoryProvider, times(1)).getLocalized(GoodCategory.MEAT)
    }

    @Test
    fun `test getNormalized() method`() {
        supermarketCategoryManager.getNormalized(SupermarketCode.LENTA, GoodCategory.MEAT.toString())
        verify(lentaCategoryProvider, times(1)).getNormalized(GoodCategory.MEAT.toString())

        supermarketCategoryManager.getNormalized(SupermarketCode.OKEY, GoodCategory.MEAT.toString())
        verify(okeyCategoryProvider, times(1)).getNormalized(GoodCategory.MEAT.toString())
    }

    @Test
    fun `test getAllCategories() method`() {
        supermarketCategoryManager.getAllCategories(SupermarketCode.LENTA)
        verify(lentaCategoryProvider, times(1)).getAllCategories()

        supermarketCategoryManager.getAllCategories(SupermarketCode.OKEY)
        verify(okeyCategoryProvider, times(1)).getAllCategories()
    }
}