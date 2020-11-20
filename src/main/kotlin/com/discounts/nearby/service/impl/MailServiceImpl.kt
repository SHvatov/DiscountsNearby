package com.discounts.nearby.service.impl

import com.discounts.nearby.service.MailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


/**
 * Implementation of
 * @see com.discounts.nearby.service.MailService
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
@Service("mailService")
class MailServiceImpl constructor(
        private val mailSender: JavaMailSender
) : MailService {

    /**
     * Username of account from which the mailing will be carried out
     */
    @Value("\${spring.mail.username}")
    private val username: String? = null

    /**
     * Implementation of
     * @see com.discounts.nearby.service.MailService.sendMail
     * @param emailTo recipient's email address
     * @param subject subject of email
     * @param message text of email
     */
    override fun sendMail(emailTo: String, subject: String, message: String) {
        val mailMessage = SimpleMailMessage()

        mailMessage.setFrom(username!!)
        mailMessage.setTo(emailTo)
        mailMessage.setSubject(subject)
        mailMessage.setText(message)

        mailSender.send(mailMessage)
    }
}