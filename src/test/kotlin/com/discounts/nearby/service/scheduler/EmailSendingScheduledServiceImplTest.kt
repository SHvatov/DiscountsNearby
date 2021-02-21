package com.discounts.nearby.service.scheduler

import com.discounts.nearby.model.*
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.MailService
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import com.discounts.nearby.service.scheduler.impl.EmailSendingScheduledServiceImpl
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate

/**
 * @author shvatov
 */
@ExtendWith(MockitoExtension::class)
internal class EmailSendingScheduledServiceImplTest {
    @Mock
    private lateinit var supermarketService: SupermarketService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var mailService: MailService

    @InjectMocks
    private lateinit var service: EmailSendingScheduledServiceImpl

    @BeforeEach
    fun setUp() {
        whenever(userService.findUsersToNotify()).thenReturn(USERS)
        whenever(supermarketService.findAll()).thenReturn(SUPERMARKETS)
    }

    @Test
    fun `send email`() {
        service.runScheduled()

        verify(supermarketService, times(1)).findAll()
        verify(userService, times(1)).findUsersToNotify()
        argumentCaptor<String> {
            verify(mailService, times(1)).sendMail(capture(), capture(), capture())
            assertEquals(USERS[0].email, firstValue)
            assertEquals(EXPECTED_SUBJECT, secondValue)
            assertTrue { thirdValue.isNotBlank() }
        }
    }

    private companion object {
        val EXPECTED_SUBJECT = "Скидки за ${LocalDate.now()}"

        val USERS = listOf(
            User().apply {
                id = "USER1"
                email = "SomeEmail"
                preferences = UserPreferences(
                    notificationsEnabled = true,
                    searchRadius = BigDecimal.ONE,
                    setOf(GoodCategory.BEER)
                )
            }
        )

        val SUPERMARKETS = listOf(
            Supermarket().apply {
                id = 1L
                code = SupermarketCode.OKEY
                name = "Okey"
                goodsSortedByDiscount = Goods(
                    listOf(
                        Good(
                            name = "Beer",
                            goodCategory = GoodCategory.BEER,
                            weight = "1kg",
                            discount = BigDecimal.TEN,
                            price = BigDecimal.ONE
                        )
                    )
                )
                goodsSortedByPrice = Goods(
                    listOf(
                        Good(
                            name = "Meat",
                            goodCategory = GoodCategory.MEAT,
                            weight = "1kg",
                            discount = BigDecimal.ONE,
                            price = BigDecimal.TEN
                        )
                    )
                )
            }
        )
    }
}