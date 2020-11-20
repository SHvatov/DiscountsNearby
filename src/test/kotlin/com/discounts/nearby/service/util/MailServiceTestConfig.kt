package com.discounts.nearby.service.util

import com.discounts.nearby.service.MailService
import com.discounts.nearby.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailServiceTestConfig {

    @Value("\${spring.mail.host}")
    private val host: String? = null

    /**
     * Port used by third party email service
     */
    @Value("\${spring.mail.port}")
    private val port = 0

    /**
     * Protocol used by third party email service
     */
    @Value("\${spring.mail.protocol}")
    private val protocol: String? = null

    @Bean
    fun mailSender(): JavaMailSender? {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        val properties = mailSender.javaMailProperties
        properties.setProperty("mail.transport.protocol", protocol)
        return mailSender
    }

    @Bean
    fun mailService(mailSender: JavaMailSender?): MailService? = MailServiceImpl(mailSender!!)
}
