package com.musinsa.board.posts.post.exception

import com.musinsa.board.system.error.BaseException
import com.musinsa.board.system.error.I18nErrorCode
import org.springframework.http.HttpStatus

class PostNotFoundException: BaseException(
    httpStatus = HttpStatus.NOT_FOUND,
    i18nErrorCode = I18nErrorCode.POST_NOT_FOUND
)