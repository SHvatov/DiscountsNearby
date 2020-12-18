package com.discounts.nearby.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.EnableScheduling


/**
 * Configuration for JavaMailSender bean
 * @author Created by Vladislav Marchenko on 19.11.2020
 */
@Configuration
@EnableScheduling
class MailConfig {
    /**
     * Host used by third party email service
     */
    @Value("\${spring.mail.host}")
    private val mailHost: String? = null

    /**
     * Username of account from which the mailing will be carried out
     */
    @Value("\${spring.mail.username}")
    private val mailUsername: String? = null

    /**
     * Password from account from which the mailing will be carried out
     */
    @Value("\${spring.mail.password}")
    private val mailPassword: String? = null

    /**
     * Port used by third party email service
     */
    @Value("\${spring.mail.port}")
    private val mailPort = 0

    /**
     * Protocol used by third party email service
     */
    @Value("\${spring.mail.protocol}")
    private val mailProtocol: String? = null

    @Bean
    fun mailSender(): JavaMailSender = JavaMailSenderImpl().apply {
        host = mailHost
        port = mailPort
        username = mailUsername
        password = mailPassword
        javaMailProperties["mail.transport.protocol"] = mailProtocol
    }

}