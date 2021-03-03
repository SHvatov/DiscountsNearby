package com.discounts.nearby.controller

import com.discounts.nearby.model.User
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


/**
 * @author Created by Vladislav Marchenko on 03.03.2021
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
internal class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var supermarketService: SupermarketService


    @Test
    @WithMockUser(authorities = ["READ", "WRITE"])
    fun `test user settings page when authorized`() {
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(CATS)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/${NOT_NULL_USER_ID}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/userSettingsPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(
                MockMvcResultMatchers.model().attribute(
                    "signInData",
                    FIRST_TEST_MODEL_VALUE
                )
            )
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
    fun `test user settings page when not authorized`() {
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(CATS)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/${NOT_NULL_USER_ID}"))
            .andExpect(MockMvcResultMatchers.status().isFound)
            .andReturn()
    }

    @Test
    fun updateUserSettingsPage() {
    }

    private companion object {

        const val NOT_NULL_USER_ID = "123"

        val CATS = listOf(GoodCategory.MEAT, GoodCategory.BEER, GoodCategory.NO_CATEGORY).map { it.toString() }

        val NOT_NULL_USER = User().apply {
            this.id = NOT_NULL_USER_ID
            this.email = "ex@mail.ru"
            this.name = "Name"
            this.preferences = null
        }

        val FIRST_TEST_MODEL_VALUE = mapOf(
            Pair("categories", CATS),
            Pair("user", NOT_NULL_USER)
        )

    }
}