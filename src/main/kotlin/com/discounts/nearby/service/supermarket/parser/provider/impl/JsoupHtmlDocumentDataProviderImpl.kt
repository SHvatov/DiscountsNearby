package com.discounts.nearby.service.supermarket.parser.provider.impl

import com.discounts.nearby.service.supermarket.parser.provider.JsoupHtmlDocumentDataProvider
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("jsoupHtmlDocumentDataProvider")
class JsoupHtmlDocumentDataProviderImpl : JsoupHtmlDocumentDataProvider {
    override fun getDocumentDataByUrl(url: String): Document =
        runCatching {
            Jsoup.connect(url).get()
        }.getOrNull() ?: Document.createShell(url)
}