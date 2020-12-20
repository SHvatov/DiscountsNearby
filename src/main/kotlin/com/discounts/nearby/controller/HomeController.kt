package com.discounts.nearby.controller

import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.model.User
import com.discounts.nearby.service.SupermarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*


/**
 * @author Created by Vladislav Marchenko on 19.11.2020
 */
@Controller
class HomeController @Autowired constructor(
        private val supermarketService: SupermarketService
) {
    @GetMapping("/")
    fun homePage(model: Model, @AuthenticationPrincipal user: User?): String {
        val data: MutableMap<String, Any?> = HashMap()

        val lentaGoods = supermarketService
                .getAllCategoriesData(SupermarketCode.LENTA, 5L, true)

        val okeyGoods = supermarketService
                .getAllCategoriesData(SupermarketCode.OKEY, 5L, true)


        data["user"] = user

        data["lentaGoods"] = lentaGoods

        data["okeyGoods"] = okeyGoods


        model.addAttribute("signInData", data)

        return "/homePage"
    }
}