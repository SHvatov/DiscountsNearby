package com.discounts.nearby

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Startup point of the whole web-application
 * @author shvatov
 */
@SpringBootApplication
class DiscountsNearbyApplication

fun main(args: Array<String>) {
    runApplication<DiscountsNearbyApplication>(*args)
}
