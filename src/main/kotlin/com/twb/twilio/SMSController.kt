package com.twb.twilio


import com.twilio.http.TwilioRestClient

import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber

//import com.twilio.twiml.Body
//import com.twilio.twiml.Message
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Body
import com.twilio.twiml.messaging.Message
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class SMSController {

    @Value("\${TWILIO_ACCOUNT_SID}")
    private lateinit var accountSid: String

    @Value("\${TWILIO_AUTH_TOKEN}")
    private lateinit var authToken: String

    @Value("\${TWILIO_PHONE_NUMBER}")
    private lateinit var phoneNumber: String

    @RequestMapping( "/")
    fun helloSpringBoot() = "Hello Spring Boot"

    @RequestMapping("/sendMessage/to/{toPhone}")
    fun sendMessage(@PathVariable toPhone: String) : com.twilio.rest.api.v2010.account.Message{ //

        val client = TwilioRestClient.Builder(accountSid, authToken).build()

        val message = MessageCreator(
                PhoneNumber(toPhone),
                PhoneNumber(phoneNumber),
                "Look ma I’m type inferred!").create(client)

        println(message.sid)
        return message
    }

    @RequestMapping(value = arrayOf("/replyMessage"), produces = arrayOf("text/xml"))
    fun replyMessage(req: HttpServletRequest, resp: HttpServletResponse): String? {
        println(req.toString())
//        println(req.getParameter("Body"))
        val text = "Be getting back to you soon, let me do some more Kotlin first"
        println(text)
        val message = Message.Builder().body(Body.Builder(text).build()).build();
        return MessagingResponse.Builder().message(message).build().toXml();
    }
}