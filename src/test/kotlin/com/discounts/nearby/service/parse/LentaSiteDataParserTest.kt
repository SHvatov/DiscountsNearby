package com.discounts.nearby.service.parse

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.model.category.LentaCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.parser.provider.JsoupHtmlDocumentDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.impl.LentaSiteDataProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File
import java.math.BigDecimal

/**
 * @author shvatov
 */
@ExtendWith(MockitoExtension::class)
internal class LentaSiteDataParserTest {
    @Mock
    private lateinit var goodCategoryManager: SupermarketCategoryManager

    @Mock
    private lateinit var documentDataProvider: JsoupHtmlDocumentDataProvider

    @InjectMocks
    private lateinit var service: LentaSiteDataProvider

    private val lentaPageHtml: String = StringBuilder().also { builder ->
        File(PATH_TO_HTML).forEachLine {
            builder.append(it)
        }
    }.toString()

    @BeforeEach
    fun setUp() {
        whenever(goodCategoryManager.getLocalized(SupermarketCode.LENTA, GoodCategory.BEER))
            .thenReturn(LentaCategory.BEER.localizedCategory)
        whenever(documentDataProvider.getDocumentDataByUrl(any()))
            .thenAnswer {
                val url = it.getArgument<String>(0)
                Jsoup.parse(lentaPageHtml).apply {
                    setBaseUri(url)
                }
            }
    }

    @Test
    fun `test parsing the website - all elements`() {
        val goods = service.getDataByCategory(GoodCategory.BEER, 10, false)
        assertEquals(10, goods.goods!!.size)
        assertTrue { goods.goods!!.all { it.goodCategory == GoodCategory.BEER } }
        verify(documentDataProvider, times(1)).getDocumentDataByUrl(any())
    }

    @Test
    fun `test parsing the website - discounts only`() {
        val goods = service.getDataByCategory(GoodCategory.BEER, 10, true)
        assertEquals(10, goods.goods!!.size)
        assertTrue { goods.goods!!.all { it.goodCategory == GoodCategory.BEER } }
        assertTrue { goods.goods!!.all { it.discount != BigDecimal.ZERO } }
        verify(documentDataProvider, times(1)).getDocumentDataByUrl(any())
    }

    private companion object {
        const val PATH_TO_HTML = "src/test/resources/lenta.html"
    }
}