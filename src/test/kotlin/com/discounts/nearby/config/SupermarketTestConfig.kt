package com.discounts.nearby.config

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.supermarket.parser.provider.impl.LentaSiteDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.impl.OkeySiteDataProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * @author Created by Vladislav Marchenko on 03.03.2021
 */
@TestConfiguration
class SupermarketTestConfig {
    @Bean
    @Primary
    fun mockLentaProvider(): LentaSiteDataProvider {
        val provider = mock<LentaSiteDataProvider>()
        whenever(provider.supermarketCode).thenReturn(SupermarketCode.LENTA)
        return provider
    }

    @Bean
    @Primary
    fun mockOkeyProvider(): OkeySiteDataProvider {
        val provider = mock<OkeySiteDataProvider>()
        whenever(provider.supermarketCode).thenReturn(SupermarketCode.OKEY)
        return provider
    }
}