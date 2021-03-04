package com.discounts.nearby.service.supermarket.parser.provider.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.parser.provider.JsoupHtmlDocumentDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * @author shvatov
 */
@Service("okeySiteDataParser")
class OkeySiteDataProvider @Autowired constructor(
    documentDataProvider: JsoupHtmlDocumentDataProvider,
    goodCategoryManager: SupermarketCategoryManager
) : AbstractSupermarketSiteDataProviderImpl(documentDataProvider, goodCategoryManager), SupermarketSiteDataProvider {
    override val supermarketCode = SupermarketCode.OKEY

    override val productClass = PRODUCT_CLASS

    override val productContainer = PRODUCT_CONTAINER

    override val isPaginationSupported = true

    override fun getConnectionUrlByCategory(
        localizedCategory: String,
        sortedByDiscount: Boolean,
        page: Int
    ): String {
        val beginIndex = page * ELEMENTS_PER_PAGE
        return "$CONNECTION_URL/$localizedCategory" +
            "#facet:&productBeginIndex:$beginIndex&pageSize=72&orderBy:&pageView:grid&minPrice:&maxPrice:&pageSize:72&"
    }

    override fun getConnectionUrlByGoodName(
        goodName: String,
        sortedByDiscount: Boolean,
        page: Int
    ): String {
        val beginIndex = page * ELEMENTS_PER_PAGE
        return "$CONNECTION_URL/webapp/wcs/stores/servlet/SearchDisplay?categoryId=&storeId=10653&catalogId=12052" +
            "&langId=-20&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true" +
            "&searchSource=Q&pageView=&beginIndex=$beginIndex&pageSize=72&searchTerm=${goodName.replace(" ", "+")}" +
            "#facet:&productBeginIndex:0&orderBy:3&pageView:&minPrice:&maxPrice:&pageSize:72&"
    }

    override fun parseProduct(goodCategory: GoodCategory, productElement: Element): Good {
        val name = productElement
            .select("div.product-name")
            .select("a")
            .attr("title")

        val pathToImage = productElement
            .select("div.product-image")
            .select("img")
            .attr("data-src")

        val weight = productElement
            .select("div.product-weight")[0]
            .text().trim()

        val priceElements = productElement.select("div.product-price")
        val discountedPriceElem = priceElements.select("span.price.label.label-red")
        val oldPriceElem = priceElements.select("span.label.small.crossed")
        val regularPriceElem = priceElements.select("span.price.label")
        val (price, oldPrice) = if (discountedPriceElem.isEmpty() && oldPriceElem.isEmpty()) {
            convertToCurrency(regularPriceElem[0].text()) to BigDecimal.ZERO
        } else {
            convertToCurrency(discountedPriceElem[0].text()) to convertToCurrency(oldPriceElem[0].text())
        }

        val discount = if (oldPrice != BigDecimal.ZERO) {
            (oldPrice - price) / oldPrice * BigDecimal(100L)
        } else BigDecimal.ZERO
        return Good(name, pathToImage, goodCategory, weight, discount, price)
    }

    private fun convertToCurrency(currency: String): BigDecimal {
        return BigDecimal(
            currency.replace(RUBBLE, "")
                .replace(",", ".")
                .trim()
        )
    }

    private companion object {
        const val CONNECTION_URL = "https://www.okeydostavka.ru/spb"

        const val PRODUCT_CLASS = "product.ok-theme"

        const val PRODUCT_CONTAINER = "div"

        const val ELEMENTS_PER_PAGE = 72L

        const val RUBBLE = "₽"
    }
}