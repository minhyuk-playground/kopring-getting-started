package com.musinsa.board.system.error

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.ZonedDateTime
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class RestErrorHandler(
    private val messageSource: MessageSource
) {
    @ExceptionHandler(MethodArgumentNotValidException::class, BindException::class)
    fun handleMethodArgumentValidationExceptionOrBindException(
        e: Exception
    ): ResponseEntity<WebErrorMessage> {
        val bindingResult: BindingResult = getBindingResult(e)
        val i18nErrorCode: String = bindingResult.fieldError?.defaultMessage ?: I18nErrorCode.UNKNOWN.code
        return loggingAndCreateErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            i18nErrorCode = i18nErrorCode,
            throwable = e
        )
    }

    private fun getBindingResult(
        e: Exception
    ): BindingResult =
        when (e) {
            is MethodArgumentNotValidException -> e.bindingResult
            is BindException -> e.bindingResult
            else -> throw UnsupportedOperationException()
        }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        e: ConstraintViolationException
    ): ResponseEntity<WebErrorMessage> {
        val i18nErrorCode: String = e.constraintViolations.firstOrNull()?.message ?: I18nErrorCode.UNKNOWN.code
        return loggingAndCreateErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            i18nErrorCode = i18nErrorCode,
            throwable = e
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
    ): ResponseEntity<WebErrorMessage> =
        loggingAndCreateErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            i18nErrorCode = I18nErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.code,
            i18nErrorMessageArgs = arrayOf(e.name),
            throwable = e
        )

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        e: MissingServletRequestParameterException
    ): ResponseEntity<WebErrorMessage> =
        loggingAndCreateErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            i18nErrorCode = I18nErrorCode.MISSING_REQUEST_PARAMETER.code,
            i18nErrorMessageArgs = arrayOf(e.parameterName),
            throwable = e
        )


    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        e: Exception
    ): ResponseEntity<WebErrorMessage> =
        loggingAndCreateErrorResponse(
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            i18nErrorCode = I18nErrorCode.UNKNOWN.code,
            throwable = e
        )

    private fun loggingAndCreateErrorResponse(
        httpStatus: HttpStatus,
        i18nErrorCode: String,
        i18nErrorMessageArgs: Array<*>? = null,
        throwable: Throwable,
    ): ResponseEntity<WebErrorMessage> {
        val i18nErrorMessage = getI18nErrorMessage(i18nErrorCode, i18nErrorMessageArgs)
        logger.error("error message : $i18nErrorMessage", throwable)
        return ResponseEntity.status(httpStatus).body(
            WebErrorMessage(
                httpStatus = httpStatus,
                errorCode = i18nErrorCode,
                message = i18nErrorMessage,
                timestamp = ZonedDateTime.now(),
            )
        )
    }

    private fun getI18nErrorMessage(
        i18nErrorCode: String,
        errorMessageArgs: Array<*>?,
    ): String = messageSource.getMessage(i18nErrorCode, errorMessageArgs, LocaleContextHolder.getLocale())

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RestErrorHandler::class.java)
    }
}