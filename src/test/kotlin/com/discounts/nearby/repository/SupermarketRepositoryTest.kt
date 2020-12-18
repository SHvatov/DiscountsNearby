package com.discounts.nearby.repository

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.Goods
import com.discounts.nearby.model.Supermarket
import com.discounts.nearby.model.SupermarketCode
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
class SupermarketRepositoryTest {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var supermarketRepository: SupermarketRepository

    @Test
    fun `find entity by id`() {
        val toPersist = Supermarket().apply {
            name = "name"
            code = SupermarketCode.OKEY
            goodsSortedByPrice = Goods(
                goods = listOf(
                    Good(
                        name = "good",
                        pathToPicture = "path/to/picture",
                        weight = "1 kg.",
                        discount = BigDecimal.ZERO,
                        price = BigDecimal.TEN
                    )
                )
            )
            goodsSortedByDiscount = Goods(
                goods = emptyList()
            )
        }

        val persisted = entityManager
            .persist(toPersist)
            .also { entityManager.flush() }

        val found = supermarketRepository.findByIdOrNull(persisted.id)

        assertNotNull(found)
        assertEquals(persisted, found)
    }
}