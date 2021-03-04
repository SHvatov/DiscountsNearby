package com.discounts.nearby.service.supermarket.parser.provider

/**
 * @author shvatov
 */
interface HtmlDocumentDataProvider<T : Any> {
    /**
     * Returns a html document located by provided [url].
     */
    fun getDocumentDataByUrl(url: String): T
}