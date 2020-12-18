package com.discounts.nearby.controller

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.category.GoodCategory
import com.discounts.nearby.service.supermarket.parser.manager.impl.SupermarketSiteDataManagerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Created by Vladislav Marchenko on 18.12.2020
 */
@Controller
@RequestMapping(value = ["/supermarkets"])
class SupermarketController @Autowired constructor(
        //TODO change SupermarketSiteDataManagerImpl to SupermarketServiceImpl
        // when scheduled shops info parser gonna be done
        private val service: SupermarketSiteDataManagerImpl
) {

    @PostMapping(name = "/allCategoriesData", value = ["/allCategoriesData"])
    fun getAllCategoriesData(@RequestParam supermarketCode: String,
                             @RequestParam elementsToFetch: Int,
                             @RequestParam discountOnly: Boolean): List<Good> {
        return service.getAllCategoriesData(SupermarketCode.valueOf(supermarketCode), elementsToFetch, discountOnly).getValue(GoodCategory.BEER).goods!!
    }
}