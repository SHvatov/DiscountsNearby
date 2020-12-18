package com.discounts.nearby.service.impl

import com.discounts.nearby.model.AbstractEntity
import com.discounts.nearby.service.CrudService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.io.Serializable

/**
 * @author shvatov
 */
abstract class AbstractCrudService<R, E, ID> constructor(
    protected val repository: R
) : CrudService<E, ID> where R : JpaRepository<E, ID>,
                             E : AbstractEntity<ID>,
                             ID : Serializable {
    override fun save(entity: E) = repository.save(entity)

    override fun delete(id: ID) = repository.deleteById(id)

    override fun findById(id: ID) = repository.findByIdOrNull(id)

    override fun findAll(): List<E> = repository.findAll()

    override fun find(page: Int?,
                      pageSize: Int?,
                      entityFilter: (E) -> Boolean): List<E> {
        if ((page == null && pageSize != null) || (page != null && pageSize == null)) {
            throw IllegalArgumentException("Both page and pageSize must be present in case pagination is required")
        } else if (page != null && pageSize != null && (page <= 0 || pageSize <= 0)) {
            throw IllegalArgumentException("Both page and pageSize must be positive integers")
        }

        // eager fetching because all entities are stored in memory anyway
        val filteredEntities = findAll().filter { entityFilter(it) }
        return if (page != null && pageSize != null) {
            filteredEntities.drop(page * pageSize).take(pageSize)
        } else filteredEntities
    }
}