package com.discounts.nearby.controller

import com.discounts.nearby.model.UserPreferences
import com.discounts.nearby.model.category.GoodCategory
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
 * @author Created by Vladislav Marchenko on 19.12.2020
 */
@Controller
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val supermarketService: SupermarketService
) {
    @GetMapping("/{userId}")
    fun getUserSettingsPage(model: Model, @PathVariable userId: String): String {
        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = userService.findById(userId)

        data["categories"] = supermarketService.getAllCategoriesNames()

        model.addAttribute("signInData", data)

        return "/userSettingsPage"
    }

    @GetMapping("/update/{user}")
    fun updateUserSettingsPage(model: Model,
                               @PathVariable user: String): String {
        val data: MutableMap<String, Any?> = HashMap()

        val params = user.split(":")

        if (params.size != 4) {
            throw IllegalArgumentException()
        }

        val userId = params[0]

        val searchRadius = params[1].toBigDecimalOrNull() ?: error("Ya pidor")

        val notificationsEnabled = params[2].toBoolean()

        val categories = if (params[3] != "")
            params[3].split(",").map { GoodCategory.valueOf(it) }.toSet()
        else
            null

        val ourUser = userService.findById(userId)

        ourUser?.preferences = UserPreferences().apply {
            this.notificationsEnabled = notificationsEnabled
            this.searchRadius = searchRadius
            this.favouriteCategories = categories
        }

        data["user"] = userService.save(ourUser!!)

        data["categories"] = supermarketService.getAllCategoriesNames()

        model.addAttribute("signInData", data)

        return "/userSettingsPage"
    }
}