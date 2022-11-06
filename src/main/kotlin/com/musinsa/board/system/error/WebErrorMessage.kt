package com.musinsa.board.system.error

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

open class WebErrorMessage(
    val httpStatus: HttpStatus,
    val errorCode: String,
    val message: String,
    val timestamp: ZonedDateTime = ZonedDateTime.now()
)
