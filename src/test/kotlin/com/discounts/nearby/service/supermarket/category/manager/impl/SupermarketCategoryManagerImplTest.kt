package com.discounts.nearby.service.supermarket.category.manager.impl

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.provider.SupermarketCategoryProvider
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * @author Created by Vladislav Marchenko on 03.03.2021
 */
@ExtendWith(MockitoExtension::class)
internal class SupermarketCategoryManagerImplTest {

    @MockBean(name = "lentaCategoryProvider")
    lateinit var lentaCategoryProvider: SupermarketCategoryProvider

    @MockBean(name = "okeyCategoryProvider")
    lateinit var okeyCategoryProvider: SupermarketCategoryProvider

    /* @Mock
    var categoryProviders = listOf(mock(LentaCategoryProvider::class.java), mock(OkeyCategoryProvider::class.java))
*/
    @InjectMocks
    lateinit var supermarketCategoryManager: SupermarketCategoryManagerImpl

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