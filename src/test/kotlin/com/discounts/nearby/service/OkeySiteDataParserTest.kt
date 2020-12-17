package com.discounts.nearby.service

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.model.category.OkeyCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.parser.provider.impl.OkeySiteDataProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertTrue

/**
 * @author shvatov
 */
@ExtendWith(MockitoExtension::class)
internal class OkeySiteDataParserTest {
    @Mock
    private lateinit var goodCategoryManager: SupermarketCategoryManager

    @InjectMocks
    private lateinit var service: OkeySiteDataProvider

    @Test
    fun `test parsing the website - all elements`() {
        `when`(goodCategoryManager.getLocalized(SupermarketCode.OKEY, GoodCategory.BEER))
            .thenReturn(OkeyCategory.BEER.localizedCategory)

        val goods = service.getDataByCategory(GoodCategory.BEER, 10, false)
        assertTrue { goods.goods?.size != 0 }
    }

    @Test
    fun `test parsing the website - discounts only`() {
        `when`(goodCategoryManager.getLocalized(SupermarketCode.OKEY, GoodCategory.BEER))
            .thenReturn(OkeyCategory.BEER.localizedCategory)

        val goods = service.getDataByCategory(GoodCategory.BEER, 10, true)
        assertTrue { goods.goods?.size != 0 }
    }
}