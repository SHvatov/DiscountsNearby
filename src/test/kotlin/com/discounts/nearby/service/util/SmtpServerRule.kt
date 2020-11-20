package com.discounts.nearby.service.util

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.junit.rules.ExternalResource
import javax.mail.internet.MimeMessage

/**
 * @author Created by Vladislav Marchenko on 20.11.2020
 */
class SmtpServerRule : ExternalResource() {
    private var smtpServer: GreenMail? = null

    @Throws(Throwable::class)
    override fun before() {
        super.before()
        smtpServer = GreenMail(ServerSetup(2525, null, "smtp"))
        smtpServer!!.start()
    }

    val messages: Array<MimeMessage>
        get() = smtpServer!!.receivedMessages

    override fun after() {
        super.after()
        smtpServer!!.stop()
    }
}