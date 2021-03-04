package com.discounts.nearby.service.parse

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.model.category.OkeyCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.parser.provider.JsoupHtmlDocumentDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.impl.OkeySiteDataProvider
import com.nhaarman.mockitokotlin2.any
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
internal class OkeySiteDataParserTest {
    @Mock
    private lateinit var goodCategoryManager: SupermarketCategoryManager

    @Mock
    private lateinit var documentDataProvider: JsoupHtmlDocumentDataProvider

    @InjectMocks
    private lateinit var service: OkeySiteDataProvider

    private val okeyPageHtml: String = StringBuilder().also { builder ->
        File(PATH_TO_HTML).forEachLine {
            builder.append(it)
        }
    }.toString()

    @BeforeEach
    fun setUp() {
        whenever(goodCategoryManager.getLocalized(SupermarketCode.OKEY, GoodCategory.BEER))
            .thenReturn(OkeyCategory.BEER.localizedCategory)
        whenever(documentDataProvider.getDocumentDataByUrl(any()))
            .thenAnswer {
                val url = it.getArgument<String>(0)
                Jsoup.parse(okeyPageHtml).apply {
                    setBaseUri(url)
                }
            }
    }

    @Test
    fun `test parsing the website - all elements`() {
        val goods = service.getDataByCategory(GoodCategory.BEER, 10, false)
        assertEquals(10, goods.goods!!.size)
        assertTrue { goods.goods!!.all { it.goodCategory == GoodCategory.BEER } }
        assertTrue { goods.goods?.size != 0 }
    }

    @Test
    fun `test parsing the website - discounts only`() {
        val goods = service.getDataByCategory(GoodCategory.BEER, 10, true)
        assertEquals(10, goods.goods!!.size)
        assertTrue { goods.goods!!.all { it.goodCategory == GoodCategory.BEER } }
        assertTrue { goods.goods!!.all { it.discount != BigDecimal.ZERO } }
        assertTrue { goods.goods?.size != 0 }
    }

    private companion object {
        const val PATH_TO_HTML = "src/test/resources/okey.html"
    }
}