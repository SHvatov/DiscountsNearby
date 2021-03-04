package com.discounts.nearby.service.supermarket.parser.manager.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider
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
internal class SupermarketSiteDataManagerImplTest {

    @Mock
    lateinit var lentaDataProvider: SupermarketSiteDataProvider

    @Mock
    lateinit var okeyDataProvider: SupermarketSiteDataProvider

    @InjectMocks
    lateinit var supermarketDataManager: SupermarketSiteDataManagerImpl

    @BeforeEach
    fun setUp() {
        whenever(lentaDataProvider.supermarketCode).thenReturn(SupermarketCode.LENTA)
        whenever(okeyDataProvider.supermarketCode).thenReturn(SupermarketCode.OKEY)
        supermarketDataManager.setProviders(listOf(lentaDataProvider, okeyDataProvider))
    }

    @Test
    fun getAllCategoriesData() {
        supermarketDataManager.getAllCategoriesData(SupermarketCode.LENTA, 4, true)
        verify(lentaDataProvider, times(1)).getAllCategoriesData(4, true)

        supermarketDataManager.getAllCategoriesData(SupermarketCode.OKEY, 4, true)
        verify(okeyDataProvider, times(1)).getAllCategoriesData(4, true)
    }

    @Test
    fun getDataByCategory() {
        supermarketDataManager.getDataByCategory(SupermarketCode.LENTA, GoodCategory.MEAT, 4, true)
        verify(lentaDataProvider, times(1)).getDataByCategory(GoodCategory.MEAT, 4, true)

        supermarketDataManager.getDataByCategory(SupermarketCode.OKEY, GoodCategory.MEAT, 4, true)
        verify(okeyDataProvider, times(1)).getDataByCategory(GoodCategory.MEAT, 4, true)
    }
}