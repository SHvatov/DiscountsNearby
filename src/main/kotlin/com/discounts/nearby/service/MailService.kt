package com.discounts.nearby.service

/**
 * Service for sending emails
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
interface MailService {
    /**
     * Sends the email with the following [subject] and [message] to the receiver with [emailTo]
     */
    fun sendMail(emailTo : String, subject : String, message : String)
}