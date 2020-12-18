package com.discounts.nearby.service.supermarket.parser.provider.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
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
    goodCategoryManager: SupermarketCategoryManager
) : AbstractSupermarketSiteDataProviderImpl(goodCategoryManager), SupermarketSiteDataProvider {
    override val supermarketCode = SupermarketCode.OKEY

    override val productClass = PRODUCT_CLASS

    override val productContainer = PRODUCT_CONTAINER

    override val isPaginationSupported = true

    override fun getConnectionUrl(localizedCategory: String,
                                  sortedByDiscount: Boolean,
                                  page: Int): String {
        val beginIndex = page * ELEMENTS_PER_PAGE
        return "$CONNECTION_URL/$localizedCategory" +
            "#facet:&productBeginIndex:$beginIndex&orderBy:3&pageView:grid&minPrice:&maxPrice:&pageSize:&"
    }

    override fun parseProduct(goodCategory: GoodCategory, productElement: Element): Good {
        val name = productElement
            .select("div.product-name")
            .select("a")[0]
            .text()

        val pathToImage = productElement
            .select("div.product-image")
            .select("a")[0]
            .absUrl("href")

        val weight = productElement
            .select("div.product-weight")[0]
            .text().trim()

        val priceElements = productElement.select("div.product-price")
        val discountedPriceElem = priceElements.select("span.price.label.label-red")
        val oldPriceElem = priceElements.select("span.label.small.crossed")
        val regularPriceElem = priceElements.select("span.price.label")
        val (price, oldPrice) = if (discountedPriceElem.isEmpty() && oldPriceElem.isEmpty()) {
            convertToCurrency(regularPriceElem.text()) to BigDecimal.ZERO
        } else {
            convertToCurrency(discountedPriceElem[0].text()) to convertToCurrency(oldPriceElem[0].text())
        }

        val discount = if (oldPrice != BigDecimal.ZERO) {
            (oldPrice - price) / price * BigDecimal(100L)
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