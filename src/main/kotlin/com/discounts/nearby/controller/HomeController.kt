package com.discounts.nearby.controller

import com.discounts.nearby.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*


/**
 * @author Created by Vladislav Marchenko on 19.11.2020
 */
@Controller
class HomeController {
    @GetMapping("/")
    fun homePage(model: Model, @AuthenticationPrincipal user: User?): String {
        val data: MutableMap<String, Any?> = HashMap()

        data["user"] = user


        model.addAttribute("signInData", data)

        return "/homePage"
    }
}