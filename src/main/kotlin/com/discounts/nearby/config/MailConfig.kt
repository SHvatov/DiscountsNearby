package com.discounts.nearby.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl


/**
 * Configuration for JavaMailSender bean
 * @author Created by Vladislav Marchenko on 19.11.2020
 */
@Configuration
class MailConfig {
    /**
     * Host used by third party email service
     */
    @Value("\${spring.mail.host}")
    private val host: String? = null

    /**
     * Username of account from which the mailing will be carried out
     */
    @Value("\${spring.mail.username}")
    private val username: String? = null

    /**
     * Password from account from which the mailing will be carried out
     */
    @Value("\${spring.mail.password}")
    private val password: String? = null

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
        mailSender.username = username
        mailSender.password = password
        val properties = mailSender.javaMailProperties
        properties.setProperty("mail.transport.protocol", protocol)
        return mailSender
    }
}