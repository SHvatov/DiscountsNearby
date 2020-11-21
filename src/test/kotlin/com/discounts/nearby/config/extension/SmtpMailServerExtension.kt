package com.discounts.nearby.config.extension

import com.discounts.nearby.config.MailServiceTestConfig
import com.discounts.nearby.config.extension.SmtpMailServerExtension.SmtpMailServer
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.junit.jupiter.api.extension.*
import javax.mail.internet.MimeMessage

/**
 * JUnit extension, which starts and stops [GreenMail] SMTP server
 * before and after running all tests correspondingly. Also, it can inject [SmtpMailServer], which can be used
 * to get the list of the messages, that has been received by this server.
 */
class SmtpMailServerExtension : BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private var smtpServer: GreenMail = GreenMail(
        ServerSetup(
            MailServiceTestConfig.TEST_PORT,
            null,
            MailServiceTestConfig.TEST_PROTOCOL
        )
    )

    override fun supportsParameter(paramCtx: ParameterContext, extCtx: ExtensionContext): Boolean =
        paramCtx.parameter.type == SmtpMailServer::class.java

    override fun resolveParameter(paramCtx: ParameterContext, extCtx: ExtensionContext): SmtpMailServer {
        return SmtpMailServer(smtpServer)
    }

    override fun beforeAll(ctx: ExtensionContext) {
        smtpServer.start()
    }

    override fun afterAll(ctx: ExtensionContext) {
        smtpServer.stop()
    }

    /**
     * Basic data class, which stores [GreenMail] server instance and can return the list
     * of the [MimeMessage], received by this server.
     */
    class SmtpMailServer(private val smtpServer: GreenMail) {
        fun receivedMessages(): Array<MimeMessage> = smtpServer.receivedMessages
    }
}