package com.discounts.nearby.model

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * @author shvatov
 */
@MappedSuperclass
abstract class AbstractEntity<ID : Serializable> {
    abstract var id: ID?
}