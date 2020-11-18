package com.discounts.nearby.repository

import com.discounts.nearby.model.User
import com.discounts.nearby.model.UserPreferences
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal


/**
 * @author shvatov
 */
@DataJpaTest
@ExtendWith(SpringExtension::class)
class UserRepositoryTest {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `find entity by id`() {
        val toPersist = User().apply {
            id = "1234"
            name = "name"
            email = "test@mail.com"
            preferences = UserPreferences(
                notificationsEnabled = true,
                searchRadius = BigDecimal.TEN,
                favouriteCategories = emptySet()
            )
        }
        val persisted = entityManager
            .persist(toPersist)
            .also { entityManager.flush() }

        val found = userRepository.findByIdOrNull(persisted.id)

        assertNotNull(found)
        assertEquals(persisted, found)
    }
}