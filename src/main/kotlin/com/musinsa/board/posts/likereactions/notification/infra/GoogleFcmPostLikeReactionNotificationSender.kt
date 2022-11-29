package com.musinsa.board.posts.likereactions.notification.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionCreatedEvent
import com.musinsa.board.posts.likereactions.notification.domain.*
import com.musinsa.board.posts.likereactions.notification.domain.PostLikeReactionNotificationStatus.FAILED
import com.musinsa.board.posts.likereactions.notification.domain.PostLikeReactionNotificationStatus.SUCCESS
import com.musinsa.board.sharing.fcm.GoogleFcmClient
import com.musinsa.board.sharing.fcm.model.GoogleFcmPushMessage
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class GoogleFcmPostLikeReactionNotificationSender(
    private val googleFcmClient: GoogleFcmClient,
    private val postLikeReactionNotificationRepository: PostLikeReactionNotificationRepository
) : PostLikeReactionNotificationSender {
    @Async
//    @Async("eventThreadPoolTaskExecutor")
    @TransactionalEventListener(PostLikeReactionCreatedEvent::class, phase = TransactionPhase.AFTER_COMMIT)
    fun handlePostLikeReactionCreatedEvent(event: PostLikeReactionCreatedEvent) {
        sendNotification(PostLikeReactionNotificationTrial(event.id, event.likedPostId, event.reactorId))
    }

    override fun sendNotification(trial: PostLikeReactionNotificationTrial): PostLikeReactionNotification {
        val postLikeReactorId = trial.postLikedReactionId
        if (sendGoogleFcmPushNotification(postLikeReactorId)) {
            return savePostLikeReactionNotification(postLikeReactorId, SUCCESS)
        }
        return savePostLikeReactionNotification(postLikeReactorId, FAILED)
    }

    private fun sendGoogleFcmPushNotification(postLikeReactorId: Long): Boolean {
        val message = createGoogleFcmPushMessage(postLikeReactorId)
        for (i in 1..3) {
            try {
                googleFcmClient.sendPushMessage(message)
                return true
            } catch (e: Throwable) {
                if (i == 3) {
                    logger.error(e) { "fail google fcm push message." }
                }
            }
        }
        return false
    }

    private fun createGoogleFcmPushMessage(postLikeReactorId: Long): GoogleFcmPushMessage =
        GoogleFcmPushMessage("${getReactorUserId(postLikeReactorId)}님 께서 회원님의 게시물을 좋아합니다.")

    private fun getReactorUserId(reactorId: Long): String {
        // do something
        // ex) memberRepository.findById(reactorId).userId
        return "reactorUserId"
    }

    private fun savePostLikeReactionNotification(
        reactorId: Long,
        status: PostLikeReactionNotificationStatus
    ): PostLikeReactionNotification =
        postLikeReactionNotificationRepository.save(PostLikeReactionNotification(reactorId, status))

    companion object {
        private val logger = KotlinLogging.logger(GoogleFcmPostLikeReactionNotificationSender::class.java.name)
    }
}