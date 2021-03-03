package com.discounts.nearby.config

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.supermarket.parser.provider.impl.LentaSiteDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.impl.OkeySiteDataProvider
import org.mockito.Mockito
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
        Mockito.`when`(provider1.supermarketCode).thenReturn(SupermarketCode.LENTA)
        return provider1
    }

    @Bean
    @Primary
    fun mockOkeyProvider(): OkeySiteDataProvider {
        Mockito.`when`(provider2.supermarketCode).thenReturn(SupermarketCode.OKEY)
        return provider2
    }
}