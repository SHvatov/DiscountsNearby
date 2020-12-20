package com.discounts.nearby.controller

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author Created by Vladislav Marchenko on 18.12.2020
 */
@RestController
@RequestMapping("/api/supermarkets")
class SupermarketController @Autowired constructor(
        private val service: SupermarketService
) {

    @GetMapping("")
    fun getAllCategoriesDataByDiscount(@RequestParam supermarketCode: String,
                                       @RequestParam goodsNumber: Int): List<Good> = service
            .getAllCategoriesData(SupermarketCode.valueOf(supermarketCode), goodsNumber.toLong(), true)


    @GetMapping("/categories")
    fun getAllCategoriesNames() = service.getAllCategoriesNames()
}