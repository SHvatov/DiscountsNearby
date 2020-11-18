package com.discounts.nearby.repository

import com.discounts.nearby.model.Supermarket
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author shvatov
 */
interface SupermarketRepository : JpaRepository<Supermarket, Long>