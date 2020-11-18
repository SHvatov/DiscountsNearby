package com.discounts.nearby.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import javax.persistence.AttributeConverter

/**
 * @author shvatov
 */
abstract class JpaJsonConverter<E : Serializable> : AttributeConverter<E, String> {
    @Suppress("unchecked_cast")
    private val entityClass = (this.javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<E>

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attr: E?): String? =
        attr?.let { objectMapper.writeValueAsString(attr) }

    override fun convertToEntityAttribute(json: String?): E? =
        json?.let { objectMapper.readValue(json, entityClass) }
}