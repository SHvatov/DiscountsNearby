package com.discounts.nearby.service

import com.discounts.nearby.model.AbstractEntity
import java.io.Serializable
import javax.transaction.Transactional

/**
 * @author shvatov
 */
interface CrudService<E : AbstractEntity<ID>, ID : Serializable> {
    /**
     * Saves the provided [entity] to the database.
     */
    @Transactional
    fun save(entity: E): E

    /**
     * Deletes the entity with [id] from the database.
     */
    @Transactional
    fun delete(id: ID)

    /**
     * Finds the entity in the database by [id].
     */
    fun findById(id: ID): E?

    /**
     * Finds all the entities in the database.
     */
    fun findAll(): List<E>

    /**
     * Finds all the entities in the database.
     */
    fun find(page: Int? = null,
             pageSize: Int? = null,
             entityFilter: (E) -> Boolean = { _ -> true }): List<E>
}