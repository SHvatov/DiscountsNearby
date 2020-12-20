package com.discounts.nearby.controller

import com.discounts.nearby.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

/**
 * @author Created by Vladislav Marchenko on 19.12.2020
 */
@Controller
class UserController @Autowired constructor(
        private val service: UserService
) {
    @GetMapping("/api/users/{userId}")
    fun getUserSettingsPage(model: Model, @PathVariable userId: String): String {
        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = service.findById(userId)

        model.addAttribute("signInData", data)

        return "/userSettingsPage"
    }
}