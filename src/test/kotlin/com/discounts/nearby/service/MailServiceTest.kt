package com.discounts.nearby.service

import com.discounts.nearby.service.util.MailServiceTestConfig
import com.discounts.nearby.service.util.SmtpServerRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner


/**
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MailServiceTestConfig::class])
@EnableConfigurationProperties
@TestPropertySource(locations = ["classpath:application-test.properties"])
@ActiveProfiles("test")
class MailServiceTest {

    @Autowired
    lateinit var mailService: MailService

    @get:Rule
    var smtpServerRule = SmtpServerRule()

    val emailTo = "example@mail.ru"

    val subject = "subject"

    val content = "content"

    @Test
    fun sendMail() {
        mailService.sendMail(emailTo, subject, content)

        val receivedMessages = smtpServerRule.messages
        assertEquals(1, receivedMessages.size)
        val current = receivedMessages[0]

        assertEquals(subject, current.subject)
        assertEquals(emailTo, current.allRecipients[0].toString())
        assertTrue(current.content.toString().contains(content))
    }
}