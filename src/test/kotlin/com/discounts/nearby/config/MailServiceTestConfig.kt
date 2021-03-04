package com.discounts.nearby.config

import com.discounts.nearby.service.MailService
import com.discounts.nearby.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

/**
 * @author Vladislav Marchenko
 */
@Configuration
class MailServiceTestConfig {
    @Bean(name = ["testMailSender"])
    fun mailSender(): JavaMailSender = JavaMailSenderImpl().apply {
        this.host = TEST_HOST
        this.port = TEST_PORT
        this.protocol = TEST_PROTOCOL
    }

    @Bean(name = ["testMailService"])
    fun mailService(@Qualifier("testMailSender") mailSender: JavaMailSender): MailService = MailServiceImpl(mailSender)

    companion object {
        const val TEST_HOST = "localhost"

        const val TEST_PROTOCOL = "smtp"

        const val TEST_PORT = 2525
    }
}