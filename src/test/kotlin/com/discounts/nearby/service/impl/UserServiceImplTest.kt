package com.discounts.nearby.service.impl

import com.discounts.nearby.model.User
import com.discounts.nearby.model.UserPreferences
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.repository.UserRepository
import com.discounts.nearby.service.UserService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Note: test done without `MockitoExtension`, because at the moment
 * regular stubbing with @Mock throws NPE when trying to call generic methods.
 * See `onGeneric` for more info.
 * @author shvatov
 */
internal class UserServiceImplTest {
    private lateinit var repository: UserRepository

    private lateinit var service: UserService

    @Test
    fun `save data - verify call propagation to dao`() {
        repository = mock {
            onGeneric { save(any()) }.thenAnswer { it.arguments[0] as User }
        }

        service = UserServiceImpl(repository)
        service.save(User().apply { id = USER_ID })

        argumentCaptor<User> {
            verify(repository, times(1)).save(capture())
            assertEquals(USER_ID, firstValue.id)
        }
    }

    @Test
    fun `find data - correct search params`() {
        val data = (0..10).map {
            User().apply { id = "$USER_ID_PREFIX$it" }
        }.sortedBy { it.id!! }

        repository = mock {
            onGeneric { findAll() }.thenReturn(data)
        }

        service = UserServiceImpl(repository)
        val found = service.find(3, 2)

        assertEquals(2, found.size)
        assertEquals("${USER_ID_PREFIX}5", found[0].id)
        assertEquals("${USER_ID_PREFIX}6", found[1].id)
    }

    @Test
    fun `find data - incorrect params`() {
        repository = mock()
        service = UserServiceImpl(repository)

        assertThrows<IllegalArgumentException> {
            service.find(-3, 2)
        }
    }

    @Test
    fun `find users to notify`() {
        repository = mock {
            onGeneric { findAll() }.thenReturn(
                listOf(
                    User().apply {
                        id = USER_ID
                        preferences = UserPreferences(
                            notificationsEnabled = true,
                            favouriteCategories = setOf(GoodCategory.BEER)
                        )
                    },
                    User().apply {
                        id = USER_ID2
                        preferences = UserPreferences(
                            notificationsEnabled = false,
                            favouriteCategories = setOf(GoodCategory.BEER)
                        )
                    },
                    User().apply {
                        id = USER_ID3
                        preferences = UserPreferences(
                            notificationsEnabled = true
                        )
                    }
                )
            )
        }
        service = UserServiceImpl(repository)
        val found = service.findUsersToNotify()

        assertEquals(1, found.size)
        assertEquals(USER_ID, found[0].id)
    }

    private companion object {
        const val USER_ID = "SomeUserId"

        const val USER_ID2 = "SomeUserId2"

        const val USER_ID3 = "SomeUserId3"

        const val USER_ID_PREFIX = "USER_"
    }
}