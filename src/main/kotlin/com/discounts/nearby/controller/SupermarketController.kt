package com.discounts.nearby.controller

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

/**
 * @author Created by Vladislav Marchenko on 18.12.2020
 */
@Controller
@RequestMapping("/api/supermarkets")
class SupermarketController @Autowired constructor(
        private val userService: UserService,
        private val supermarketService: SupermarketService
) {


    @GetMapping("/{supermarketCode}/{userId}")
    fun getSupermarketPage(model: Model,
                           @PathVariable supermarketCode: String,
                           @PathVariable userId: String): String {

        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = if (userId == "0") null
        else userService.findById(userId)

        data["categories"] = supermarketService.getAllCategoriesNames()

        data["goods"] = supermarketService.getAllCategoriesData(SupermarketCode.valueOf(supermarketCode), 10L, true)

        data["goodsByCategories"] = supermarketService.getAllDataMapByCategories(SupermarketCode.valueOf(supermarketCode), 10L, true)

        data["shop"] = supermarketCode

        model.addAttribute("signInData", data)

        return "/supermarketsPage"
    }

}

