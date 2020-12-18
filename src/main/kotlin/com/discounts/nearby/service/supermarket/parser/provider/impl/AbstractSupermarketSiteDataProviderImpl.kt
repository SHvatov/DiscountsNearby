package com.discounts.nearby.service.supermarket.parser.provider.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.category.manager.SupermarketCategoryManager
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.math.BigDecimal

/**
 * @author shvatov
 */
abstract class AbstractSupermarketSiteDataProviderImpl constructor(
    private val goodCategoryManager: SupermarketCategoryManager
) : SupermarketSiteDataProvider {
    /**
     * Class of the html objects, that holds the products data.
     */
    protected abstract val productClass: String

    /**
     * Type of the container used to hold the date about products.
     */
    protected abstract val productContainer: String

    /**
     * Determines whether pagination is supported or not.
     */
    protected abstract val isPaginationSupported: Boolean

    override fun getAllCategoriesData(elementsToFetch: Int, discountOnly: Boolean): Map<GoodCategory, Goods> {
        return goodCategoryManager
            .getAllCategories(supermarketCode)
            .associateWith { getDataByCategory(it, elementsToFetch, discountOnly) }
    }

    override fun getDataByCategory(goodCategory: GoodCategory,
                                   elementsToFetch: Int,
                                   discountOnly: Boolean): Goods {
        val localizedCategory = goodCategoryManager.getLocalized(supermarketCode, goodCategory)
        val fetchedElements = mutableListOf<Good>()
        val pageLimit = if (isPaginationSupported) MAX_PAGES_TO_PARSE else 1

        for (page in 1..pageLimit) {
            val products = Jsoup.connect(
                getConnectionUrl(localizedCategory, discountOnly, page)
            ).get().select("${productContainer}.${productClass}")

            val goods = products.mapNotNull { element ->
                runCatching {
                    parseProduct(goodCategory, element)
                }.getOrNull()
            }.filter {
                if (discountOnly)
                    it.discount != null && it.discount != BigDecimal.ZERO
                else true
            }

            fetchedElements.addAll(goods)
        }

        val uniqueElements = mutableListOf<Good>()
        for ((_, elements) in fetchedElements.groupBy { it.name }) {
            uniqueElements.add(elements.first())
        }

        return Goods(
            goods = if (discountOnly) {
                uniqueElements.sortedByDescending { it.discount }
            } else {
                uniqueElements.sortedBy { it.price }
            }.take(elementsToFetch)
        )
    }

    /**
     * Returns the url used to fetch the html document from the [page] with
     * the goods based on the provided [localizedCategory].
     */
    protected abstract fun getConnectionUrl(localizedCategory: String,
                                            sortedByDiscount: Boolean,
                                            page: Int): String

    /**
     * Parses the provided [productElement] and builds the [Good] entity based
     * on the parsed data.
     */
    protected abstract fun parseProduct(goodCategory: GoodCategory,
                                        productElement: Element): Good

    private companion object {
        /**
         * Maximum number of elements that will be parsed during the session.
         */
        const val MAX_PAGES_TO_PARSE = 10
    }
}