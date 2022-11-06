package com.musinsa.board.posts.post.exception

import com.musinsa.board.posts.post.domain.Author
import com.musinsa.board.system.error.BaseException
import com.musinsa.board.system.error.I18nErrorCode
import org.springframework.http.HttpStatus

class IncorrectAuthorException(
    actualAuthor: Author,
    performer: Author
) : BaseException(
    httpStatus = HttpStatus.FORBIDDEN,
    i18nErrorCode = I18nErrorCode.PERMISSION_DENIED,
    logMessage = "Not the author. actual authorId : ${actualAuthor.authorId}, performer id : ${performer.authorId}"
)