package com.discounts.nearby.controller

import com.discounts.nearby.model.UserPreferences
import com.discounts.nearby.model.category.GoodCategory
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
        private val service: UserService
) {
    @GetMapping("/{userId}")
    fun getUserSettingsPage(model: Model, @PathVariable userId: String): String {
        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = service.findById(userId)

        model.addAttribute("signInData", data)

        return "/userSettingsPage"
    }

    @GetMapping("/update/{user}")
    fun updateUserSettingsPage(model: Model,
                               @PathVariable user: String): String {
        val data: MutableMap<String, Any?> = HashMap()

        var params = user.split(":")

        var userId = params[0]

        var searchRadius = params[1].toBigDecimal()

        var categories = params[2].split(",").map { GoodCategory.valueOf(it) }.toSet()

        var ourUser = service.findById(userId)

        ourUser?.preferences = UserPreferences().apply {
            this.notificationsEnabled = categories.isNotEmpty()
            this.searchRadius = searchRadius
            this.favouriteCategories = categories
        }

        data["user"] = service.save(ourUser!!)

        model.addAttribute("signInData", data)

        return "/userSettingsPage"
    }
}