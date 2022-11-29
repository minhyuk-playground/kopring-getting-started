package com.musinsa.board.posts.likereactions.likereaction.exception

import com.musinsa.board.system.error.BaseException
import com.musinsa.board.system.error.I18nErrorCode
import org.springframework.http.HttpStatus

class LikedPostNotFoundException : BaseException(HttpStatus.NOT_FOUND, I18nErrorCode.LIKED_POST_NOT_FOUND)