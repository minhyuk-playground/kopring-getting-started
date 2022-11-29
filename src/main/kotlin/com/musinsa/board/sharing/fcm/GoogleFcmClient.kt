package com.musinsa.board.sharing.fcm

import com.musinsa.board.sharing.fcm.model.GoogleFcmPushMessage
import mu.KotlinLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component

@Component
class GoogleFcmClient(
    restTemplateBuilder: RestTemplateBuilder
) {
    private val restTemplate = restTemplateBuilder.build()

    fun sendPushMessage(message: GoogleFcmPushMessage) {
        // do something
        logger.info { "Send Google FCM. $message" }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}