package com.discounts.nearby.controller

import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Created by Vladislav Marchenko on 18.12.2020
 */
@RestController
@RequestMapping("/api/supermarkets")
class SupermarketController @Autowired constructor(
        private val supermarketService: SupermarketService
)