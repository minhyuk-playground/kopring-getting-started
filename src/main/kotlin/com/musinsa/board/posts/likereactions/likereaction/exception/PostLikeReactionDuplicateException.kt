package com.musinsa.board.posts.likereactions.likereaction.exception

import com.musinsa.board.system.error.BaseException
import com.musinsa.board.system.error.I18nErrorCode
import org.springframework.http.HttpStatus

class PostLikeReactionDuplicateException : BaseException(
    HttpStatus.BAD_REQUEST,
    I18nErrorCode.POST_LIKE_REACTION_DUPLICATE
)