package com.discounts.nearby.service.impl

import com.discounts.nearby.model.User
import com.discounts.nearby.repository.UserRepository
import com.discounts.nearby.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author shvatov
 */
@Service("supermarketService")
class UserServiceImpl @Autowired constructor(
    repository: UserRepository
) : AbstractCrudService<UserRepository, User, String>(repository), UserService