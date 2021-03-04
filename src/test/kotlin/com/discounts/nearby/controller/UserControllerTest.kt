package com.discounts.nearby.controller

import com.discounts.nearby.model.User
import com.discounts.nearby.model.UserPreferences
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import com.nhaarman.mockitokotlin2.any
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.util.NestedServletException
import java.math.BigDecimal


/**
 * @author Created by Vladislav Marchenko on 03.03.2021
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
@TestPropertySource(locations = ["classpath:application-test.yaml"])
@ActiveProfiles("test")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/${NOT_NULL_USER_ID}"))
            .andExpect(MockMvcResultMatchers.status().isFound)
            .andReturn()
    }

    @Test
    fun `test user settings page when update valid data and not authorized`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/update/${VALID_DATA}"))
            .andExpect(MockMvcResultMatchers.status().isFound)
            .andReturn()
    }

    @Test
    @WithMockUser(authorities = ["READ", "WRITE"])
    fun `test user settings page when update valid data and authorized`() {
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)
        `when`(userService.save(any())).thenReturn(UPDATED_USER)
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(CATS)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/update/${VALID_DATA}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("/userSettingsPage"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("signInData"))
            .andExpect(
                MockMvcResultMatchers.model().attribute(
                    "signInData",
                    SECOND_TEST_MODEL_VALUE
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
        verify(userService, times(1)).save(any())

    }

    @Test
    @WithMockUser(authorities = ["READ", "WRITE"])
    fun `test user settings page when update invalid data and authorized`() {
        `when`(userService.findById(NOT_NULL_USER_ID)).thenReturn(NOT_NULL_USER)
        `when`(userService.save(any())).thenReturn(UPDATED_USER)
        `when`(supermarketService.getAllCategoriesNames()).thenReturn(CATS)

        assertThrows<NestedServletException> { mockMvc.perform(MockMvcRequestBuilders.get("/api/users/update/${INVALID_DATA_1}")) }
        assertThrows<NestedServletException> { mockMvc.perform(MockMvcRequestBuilders.get("/api/users/update/${INVALID_DATA_2}")) }

        verify(userService, times(0)).findById(NOT_NULL_USER_ID)
        verify(userService, times(0)).save(any())
    }

    private companion object {

        const val NOT_NULL_USER_ID = "123"

        val CATS = GoodCategory.values().map { it.toString() }

        const val VALID_DATA = "123:500:true:MEAT,BEER"

        const val INVALID_DATA_1 = "123:500"

        const val INVALID_DATA_2 = "123:zdorov:true:MEAT,BEER"

        val NOT_NULL_USER = User().apply {
            this.id = NOT_NULL_USER_ID
            this.email = "ex@mail.ru"
            this.name = "Name"
            this.preferences = null
        }

        val UPDATED_USER = User().apply {
            this.id = NOT_NULL_USER_ID
            this.email = "ex@mail.ru"
            this.name = "Name"
            this.preferences = UserPreferences().apply {
                this.notificationsEnabled = true
                this.searchRadius = BigDecimal(500)
                this.favouriteCategories = setOf(GoodCategory.MEAT, GoodCategory.BEER)
            }
        }

        val FIRST_TEST_MODEL_VALUE = mapOf(
            Pair("categories", CATS),
            Pair("user", NOT_NULL_USER)
        )

        val SECOND_TEST_MODEL_VALUE = mapOf(
            Pair("categories", CATS),
            Pair("user", UPDATED_USER)
        )

    }
}