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
@Service("lentaSiteDataParser")
class LentaSiteDataProvider @Autowired constructor(
    documentDataProvider: JsoupHtmlDocumentDataProvider,
    goodCategoryManager: SupermarketCategoryManager
) : AbstractSupermarketSiteDataProviderImpl(documentDataProvider, goodCategoryManager), SupermarketSiteDataProvider {
    override val supermarketCode = SupermarketCode.LENTA

    override val productClass = PRODUCT_CLASS

    override val productContainer = PRODUCT_CONTAINER

    override val isPaginationSupported = false

    override fun getConnectionUrlByCategory(
        localizedCategory: String,
        sortedByDiscount: Boolean,
        page: Int
    ) = "$CATALOG_URL/$localizedCategory/${if (!sortedByDiscount) "?sorting=ByCardPriceAsc" else ""}"

    override fun getConnectionUrlByGoodName(
        goodName: String,
        sortedByDiscount: Boolean,
        page: Int
    ) = "$SEARCH_URL${goodName.replace(" ", "%20")}"

    override fun parseProduct(goodCategory: GoodCategory, productElement: Element): Good {
        val pathToImage = productElement
            .select("div.sku-card-small__image.square")
            .select("div.square__inner")
            .select("img")[0]
            .absUrl("src")

        val discountElement = productElement
            .select("div.discount-label-small.discount-label-small--sku-card.sku-card-small__discount-label")
        val discount = if (discountElement.isNotEmpty()) {
            BigDecimal(discountElement.text().trim().replace(Regex("[\"-%]"), ""))
        } else BigDecimal.ZERO

        val name = productElement
            .select("div.sku-card-small__title")
            .text()
            .trim()

        val priceElements = productElement
            .select("div.sku-prices-block.sku-card-small__prices.sku-prices-block--small")
            .select("div.sku-prices-block__item.sku-prices-block__item--primary")
            .select("div.sku-price.sku-prices-block__price.sku-price--small.sku-price--primary")
        val priceInteger = priceElements
            .select("span.sku-price__integer")
            .text()
            .trim()
        val priceFraction = priceElements
            .select("small.sku-price__fraction")
            .text()
            .trim()
        val price = BigDecimal("${priceInteger}.${priceFraction}")

        return Good(name, pathToImage, goodCategory, NOT_STATED_PRICE, discount, price)
    }

    private companion object {
        const val CATALOG_URL = "https://lenta.com/catalog"

        const val SEARCH_URL = "https://lenta.com/search/?searchText="

        const val PRODUCT_CLASS = "sku-card-small-container"

        const val PRODUCT_CONTAINER = "div"

        const val NOT_STATED_PRICE = "Not stated"
    }
}