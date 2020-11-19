package com.discounts.nearby.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author Created by Vladislav Marchenko
 */
@Controller
class HomeController {

    @GetMapping("/")
    fun homePage() : String = "/homePage"
}