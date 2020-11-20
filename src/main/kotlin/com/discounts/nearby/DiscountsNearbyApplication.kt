package com.discounts.nearby

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Startup point of the whole web-application
 * @author shvatov
 */
@SpringBootApplication
@EnableScheduling
class DiscountsNearbyApplication

fun main(args: Array<String>) {
    runApplication<DiscountsNearbyApplication>(*args)
}
