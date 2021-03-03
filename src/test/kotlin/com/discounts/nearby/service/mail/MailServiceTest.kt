package com.discounts.nearby.service.mail

import com.discounts.nearby.config.MailServiceTestConfig
import com.discounts.nearby.config.extension.SmtpMailServerExtension
import com.discounts.nearby.service.MailService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


/**
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
@ExtendWith(value = [SpringExtension::class, SmtpMailServerExtension::class])
@SpringBootTest(classes = [MailServiceTestConfig::class])
@EnableConfigurationProperties
@TestPropertySource(locations = ["classpath:application-test.properties"])
@ActiveProfiles("test")
class MailServiceTest {
    @Autowired
    @Qualifier("testMailService")
    private lateinit var mailService: MailService

    @Test
    fun `test send mail`(mailServer: SmtpMailServerExtension.SmtpMailServer) {
        mailService.sendMail(emailTo, subject, content)

        val receivedMessages = mailServer.receivedMessages()
        assertEquals(1, receivedMessages.size)

        val current = receivedMessages[0]
        assertEquals(subject, current.subject)
        assertEquals(emailTo, current.allRecipients[0].toString())
        assertEquals(current.content.toString().trim(), content)
    }

    companion object {
        private const val emailTo = "example@mail.ru"

        private const val subject = "subject"

        private const val content = "content"
    }
}