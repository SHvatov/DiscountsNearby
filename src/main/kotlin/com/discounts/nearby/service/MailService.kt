package com.discounts.nearby.service

/**
 * Service for sending emails
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
interface MailService {
    /**
     * Method for sending emails
     * @param emailTo recipient's email address
     * @param subject subject of email
     * @param message text of email
     */
    fun sendMail(emailTo : String, subject : String, message : String)
}