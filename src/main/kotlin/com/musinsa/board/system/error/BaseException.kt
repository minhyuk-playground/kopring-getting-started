package com.musinsa.board.system.error

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

open class BaseException(
    val httpStatus: HttpStatus,
    val i18nErrorCode: I18nErrorCode,
    val i18nErrorMessageArgs: Array<*>? = null,
    val timestamp: ZonedDateTime = ZonedDateTime.now(),
    logMessage: String? = null,
    cause: Throwable? = null,
) : RuntimeException(logMessage, cause)