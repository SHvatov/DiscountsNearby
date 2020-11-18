package com.discounts.nearby.repository

import com.discounts.nearby.model.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author shvatov
 */
interface UserRepository : JpaRepository<User, String>