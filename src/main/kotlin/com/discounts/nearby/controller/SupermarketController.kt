package com.discounts.nearby.controller

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.SupermarketCode
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import com.discounts.nearby.service.supermarket.parser.provider.SupermarketSiteDataProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
    private val supermarketService: SupermarketService,
    @Qualifier("okeySiteDataParser") private val okeySiteDataProvider: SupermarketSiteDataProvider,
    @Qualifier("lentaSiteDataParser") private val lentaSiteDataProvider: SupermarketSiteDataProvider
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

        data["goodsByCategories"] =
            supermarketService.getAllDataMapByCategories(SupermarketCode.valueOf(supermarketCode), 10L, true)

        data["shop"] = supermarketCode

        model.addAttribute("signInData", data)

        return "/supermarketsPage"
    }

    @GetMapping("/bestPrice/{userId}")
    fun getBestPricePage(
        model: Model,
        @PathVariable userId: String
    ): String {

        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = if (userId == "0") null
        else userService.findById(userId)


        model.addAttribute("signInData", data)

        return "/bestPricePage"
    }

    @GetMapping("/bestPrice/{goodName}/{userId}")
    fun getBestPriceDataPage(
        model: Model,
        @PathVariable goodName: String,
        @PathVariable userId: String
    ): String {

        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = if (userId == "0") null
        else userService.findById(userId)

        val goods = mutableListOf<Good>()
        val okeyGoods = okeySiteDataProvider.getDataByGoodName(goodName, 5)
        val lentaGoods = lentaSiteDataProvider.getDataByGoodName(goodName, 5)

        okeyGoods.goods?.toMutableList()?.let { goods.addAll(it) }
        lentaGoods.goods?.toMutableList()?.let { goods.addAll(it) }

        data["goods"] = goods.sortedBy { it.price }.take(5).toList()

        data["goodName"] = goodName

        model.addAttribute("signInData", data)

        return "/bestPricePage"
    }

}

