package com.discounts.nearby.controller

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.impl.SupermarketServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author Created by Vladislav Marchenko on 18.12.2020
 */
@RestController
@RequestMapping("/supermarkets")
class SupermarketController @Autowired constructor(
        private val service: SupermarketServiceImpl
) {

    @PostMapping("/allCategoriesData")
    fun getAllCategoriesData(@RequestParam supermarketCode: String,
                             @RequestParam elementsToFetch: Int,
                             @RequestParam discountOnly: Boolean): List<Good> {
        return service.getAllCategoriesData(SupermarketCode.valueOf(supermarketCode), elementsToFetch.toLong(), discountOnly)
    }
}