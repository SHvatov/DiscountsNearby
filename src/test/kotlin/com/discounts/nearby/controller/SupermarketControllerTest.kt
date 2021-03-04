package com.discounts.nearby.controller

import com.discounts.nearby.config.SupermarketTestConfig
import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.User
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import com.discounts.nearby.service.supermarket.parser.provider.impl.LentaSiteDataProvider
import com.discounts.nearby.service.supermarket.parser.provider.impl.OkeySiteDataProvider
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal


/**
 * @author Created by Vladislav Marchenko on 01.03.2021
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
@Import(SupermarketTestConfig::class)
@TestPropertySource(locations = ["classpath:application-test.yaml"])
@ActiveProfiles("test")
internal class SupermarketControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var supermarketService: SupermarketService

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var provider1: LentaSiteDataProvider

    @Autowired
    private lateinit var provider2: OkeySiteDataProvider

    @Test
    fun `test supermarket page when user id is 0 and supermarket code is LENTA`() {
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(LENTA_CATS)
        `when`(supermarketService.getAllCategoriesData(SupermarketCode.LENTA, 10, true))
            .thenReturn(LENTA_GOODS)
        `when`(supermarketService.getAllDataMapByCategories(SupermarketCode.LENTA, 10, true))
            .thenReturn(LENTA_GOODS_BY_CATS)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/LENTA/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/supermarketsPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(MockMvcResultMatchers.model().attribute("signInData", FIRST_TEST_MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(supermarketService, times(1)).getAllCategoriesData(SupermarketCode.LENTA, 10, true)
        verify(supermarketService, times(1)).getAllCategoriesNames()
        verify(supermarketService, times(1)).getAllDataMapByCategories(SupermarketCode.LENTA, 10, true)
    }

    @Test
    fun `test supermarket page when user id is not 0 and supermarket code is OKEY`() {
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(OKEY_CATS)
        `when`(supermarketService.getAllCategoriesData(SupermarketCode.OKEY, 10, true))
            .thenReturn(OKEY_GOODS)
        `when`(supermarketService.getAllDataMapByCategories(SupermarketCode.OKEY, 10, true))
            .thenReturn(OKEY_GOODS_BY_CATS)
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/OKEY/${NOT_NULL_USER_ID}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/supermarketsPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(MockMvcResultMatchers.model().attribute("signInData", SECOND_TEST_MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(supermarketService, times(1)).getAllCategoriesData(SupermarketCode.OKEY, 10, true)
        verify(supermarketService, times(1)).getAllCategoriesNames()
        verify(supermarketService, times(1)).getAllDataMapByCategories(SupermarketCode.OKEY, 10, true)
        verify(userService, times(1)).findById(NOT_NULL_USER_ID)
    }

    @Test
    fun `test best price page when user id is 0`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/bestPrice/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/bestPricePage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(MockMvcResultMatchers.model().attribute("signInData", THIRD_TEST_MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())
    }

    @Test
    fun `test best price page when user id is not 0`() {
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/bestPrice/${NOT_NULL_USER_ID}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/bestPricePage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(MockMvcResultMatchers.model().attribute("signInData", FOURTH_TEST_MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(userService, times(1)).findById(NOT_NULL_USER_ID)
    }

    @Test
    fun `test best price page with data when user id is 0 and there is not a good with this name`() {
        `when`(provider1.getDataByGoodName(INVALID_GOOD_NAME, 5))
            .thenReturn(EMPTY_GOOD_LIST)
        `when`(provider2.getDataByGoodName(INVALID_GOOD_NAME, 5))
            .thenReturn(EMPTY_GOOD_LIST)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/bestPrice/${INVALID_GOOD_NAME}/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/bestPricePage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(MockMvcResultMatchers.model().attribute("signInData", FIFTH_TEST_MODEL_VALUE))
            .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(provider1, times(1)).getDataByGoodName(INVALID_GOOD_NAME, 5)
        verify(provider2, times(1)).getDataByGoodName(INVALID_GOOD_NAME, 5)
    }

    @Test
    fun `test best price page with data when user id is not 0 and there are goods with this name`() {
        `when`(provider1.getDataByGoodName(VALID_GOOD_NAME, 5))
            .thenReturn(Goods().apply {
                this.goods = LENTA_GOODS
            })
        `when`(provider2.getDataByGoodName(INVALID_GOOD_NAME, 5))
            .thenReturn(EMPTY_GOOD_LIST)
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)

        val result =
            mockMvc.perform(MockMvcRequestBuilders.get("/api/supermarkets/bestPrice/${VALID_GOOD_NAME}/${NOT_NULL_USER_ID}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.view().name("/bestPricePage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
                .andExpect(MockMvcResultMatchers.model().attribute("signInData", SIXTH_TEST_MODEL_VALUE))
                .andReturn()

        val mockResponse = result.response
        Assertions.assertThat(mockResponse.contentType).isEqualTo("text/html;charset=UTF-8")

        val responseHeaders: Collection<*> = mockResponse.headerNames
        Assert.assertNotNull(responseHeaders)
        Assert.assertEquals(8, responseHeaders.size)
        Assert.assertEquals("Check for Content-Type header", "Content-Language", responseHeaders.iterator().next())

        verify(userService, times(1)).findById(NOT_NULL_USER_ID)
        verify(provider1, times(1)).getDataByGoodName(VALID_GOOD_NAME, 5)
        verify(provider2, times(1)).getDataByGoodName(VALID_GOOD_NAME, 5)
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

        val LENTA_GOODS_BY_CATS = mapOf(
            Pair(GoodCategory.BEER, LENTA_GOODS)
        )

        val OKEY_GOODS_BY_CATS = mapOf(
            Pair(GoodCategory.MEAT, OKEY_GOODS)
        )

        val NULL_USER = null

        const val NOT_NULL_USER_ID = "123"

        val NOT_NULL_USER = User().apply {
            this.id = NOT_NULL_USER_ID
            this.email = "ex@mail.ru"
            this.name = "Name"
            this.preferences = null
        }

        val LENTA_CATS = listOf(GoodCategory.BEER.toString())

        val OKEY_CATS = listOf(GoodCategory.MEAT.toString())

        const val INVALID_GOOD_NAME = "Byr-byr-byr"

        const val VALID_GOOD_NAME = "Kozel"

        val EMPTY_GOOD_LIST = Goods().apply {
            this.goods = listOf()
        }

        val FIRST_TEST_MODEL_VALUE = mapOf(
            Pair("shop", SupermarketCode.LENTA.toString()),
            Pair("goods", LENTA_GOODS),
            Pair("categories", LENTA_CATS),
            Pair("user", NULL_USER),
            Pair("goodsByCategories", LENTA_GOODS_BY_CATS)
        )

        val SECOND_TEST_MODEL_VALUE = mapOf(
            Pair("shop", SupermarketCode.OKEY.toString()),
            Pair("goods", OKEY_GOODS),
            Pair("categories", OKEY_CATS),
            Pair("user", NOT_NULL_USER),
            Pair("goodsByCategories", OKEY_GOODS_BY_CATS)
        )

        val THIRD_TEST_MODEL_VALUE = mapOf(
            Pair("user", NULL_USER)
        )

        val FOURTH_TEST_MODEL_VALUE = mapOf(
            Pair("user", NOT_NULL_USER)
        )

        val FIFTH_TEST_MODEL_VALUE = mapOf(
            Pair("user", NULL_USER),
            Pair("goods", EMPTY_GOOD_LIST.goods),
            Pair("goodName", INVALID_GOOD_NAME)
        )

        val SIXTH_TEST_MODEL_VALUE = mapOf(
            Pair("user", NOT_NULL_USER),
            Pair("goods", LENTA_GOODS),
            Pair("goodName", VALID_GOOD_NAME)
        )
    }
}

