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
        val persisted = prepareEntity()
        val found = supermarketRepository.findByIdOrNull(persisted.id)

        assertNotNull(found)
        assertEquals(persisted, found)
    }

    @Test
    fun `find entity by its code`() {
        val persisted = prepareEntity()
        val found = supermarketRepository.getSupermarketByCode(SupermarketCode.OKEY)

        assertNotNull(found)
        assertEquals(persisted, found)
    }

    private fun prepareEntity(entity: Supermarket = ENTITY_TO_PERSIST) =
        entityManager
            .merge(entity)
            .also { entityManager.flush() }

    private companion object {
        val ENTITY_TO_PERSIST = Supermarket().apply {
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
    }
}