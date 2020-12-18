package com.discounts.nearby.service.scheduler.impl

import com.discounts.nearby.model.Good
import com.discounts.nearby.model.User
import com.discounts.nearby.service.MailService
import com.discounts.nearby.service.SupermarketService
import com.discounts.nearby.service.UserService
import com.discounts.nearby.service.scheduler.EmailSendingScheduledService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * @author shvatov
 */
@Service("emailSendingScheduledService")
class EmailSendingScheduledServiceImpl @Autowired constructor(
    private val supermarketService: SupermarketService,
    private val userService: UserService,
    private val mailService: MailService
) : EmailSendingScheduledService {
    @Scheduled(cron = "0 0 12 * * SUN")
    override fun runScheduled() {
        val userToNotify = userService.find {
            it.preferences?.notificationsEnabled == true
                && !it.preferences?.favouriteCategories.isNullOrEmpty()
        }

        val subject = "$MAIL_SUBJECT_TEMPLATE ${LocalDate.now()}"
        userToNotify.forEach {
            runCatching {
                mailService.sendMail(it.email!!, subject, prepareMessage(it))
            }
        }
    }

    private fun prepareMessage(user: User): String {
        val categories = user.preferences?.favouriteCategories
            ?: error("No categories provided")
        val goods = supermarketService.findAll()
            .mapNotNull { it.goodsSortedByDiscount }
            .flatMap { it.goods ?: emptyList() }
            .filter { it.goodCategory in categories }
            .sortedBy { it.discount }
            .take(GOODS_BATCH_SIZE)

        return goods.joinToString(separator = ",\n") {
            prepareGoodInfo(it)
        }
    }

    private fun prepareGoodInfo(good: Good): String {
        return with(good) {
            """
            Название: $name
            Цена:     $price
            Размер скидки: $discount
            Категория: ${goodCategory?.localizedName}
            """.trimIndent()
        }
    }

    private companion object {
        const val MAIL_SUBJECT_TEMPLATE = "Discounts digest for "

        const val GOODS_BATCH_SIZE = 5
    }
}