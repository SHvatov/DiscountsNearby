package com.discounts.nearby.controller

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.SupermarketService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal


/**
 * @author Created by Vladislav Marchenko on 25.02.2021
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
@TestPropertySource(locations = ["classpath:application-test.yaml"])
@ActiveProfiles("test")
internal class HomeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var supermarketService: SupermarketService

    @Test
    fun `test home page when status is ok`() {
        `when`(supermarketService.getAllCategoriesData(SupermarketCode.LENTA, 5, true)).thenReturn(LENTA_GOODS)
        `when`(supermarketService.getAllCategoriesData(SupermarketCode.OKEY, 5, true)).thenReturn(OKEY_GOODS)

        val result = mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("/homePage"))
            .andExpect(model().attributeExists("signInData"))
            .andExpect(model().attribute("signInData", MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        assertNotNull(responseHeaders)
        assertEquals(8, responseHeaders.size)
        assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(supermarketService, times(1)).getAllCategoriesData(SupermarketCode.LENTA, 5, true)
        verify(supermarketService, times(1)).getAllCategoriesData(SupermarketCode.OKEY, 5, true)
    }

    private companion object {
        val LENTA_GOODS = listOf(
            Good("Kozel", "path/to/pict", GoodCategory.BEER, "500 ml", BigDecimal(50), BigDecimal(500)),
            Good("Kozel Rubinowy", "path/to/pict", GoodCategory.BEER, "500 ml", BigDecimal(50), BigDecimal(500)),
        )

        val OKEY_GOODS = listOf(
            Good("Pork", "path/to/pict", GoodCategory.MEAT, "1 kg", BigDecimal(50), BigDecimal(500)),
            Good("Chicken", "path/to/pict", GoodCategory.MEAT, "900 g", BigDecimal(50), BigDecimal(500)),
        )

        val USER = null

        val MODEL_VALUE = mapOf(
            Pair("okeyGoods", OKEY_GOODS),
            Pair("lentaGoods", LENTA_GOODS),
            Pair("user", USER)
        )
    }
}