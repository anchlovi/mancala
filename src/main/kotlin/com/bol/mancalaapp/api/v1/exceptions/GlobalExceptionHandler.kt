package com.bol.mancalaapp.api.v1.exceptions

import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.VersionMismatchException
import com.bol.mancalaapp.rules.validators.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


/**
 * Global exception handler for the Mancala game application.
 * This class provides centralized handling of exceptions thrown across the application.
 * It maps specific exceptions to appropriate HTTP response statuses and formats the response body.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    /**
     * Handles exceptions of type [GameNotFoundException].
     *
     * @param e The caught [GameNotFoundException].
     * @return A ResponseEntity with an [ErrorResponse] and a NOT FOUND status.
     */
    @ExceptionHandler(GameNotFoundException::class)
    fun handleGameNotFoundException(e: GameNotFoundException): ResponseEntity<ErrorResponse> =
        handleException(e, HttpStatus.NOT_FOUND)

    /**
     * Handles exceptions of type [VersionMismatchException].
     *
     * @param e The caught [VersionMismatchException].
     * @return A ResponseEntity with an [ErrorResponse] and a CONFLICT status.
     */
    @ExceptionHandler(VersionMismatchException::class)
    fun handleVersionMismatchException(e: VersionMismatchException): ResponseEntity<ErrorResponse> =
        handleException(e, HttpStatus.CONFLICT)

    /**
     * Handles exceptions of type [ValidationException].
     *
     * @param e The caught [ValidationException].
     * @return A ResponseEntity with an [ErrorResponse] and a BAD REQUEST status.
     */
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse> =
        handleException(e, HttpStatus.BAD_REQUEST)

    /**
     * Handles exceptions of type [IllegalArgumentException].
     *
     * @param e The caught [IllegalArgumentException].
     * @return A ResponseEntity with an [ErrorResponse] and a BAD REQUEST status.
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> =
        handleException(e, HttpStatus.BAD_REQUEST)

    /**
     * A private helper method to create a [ResponseEntity] with a given [Exception] and [HttpStatus].
     *
     * @param e The exception to be handled.
     * @param status The HTTP status to be returned.
     * @return A ResponseEntity with an [ErrorResponse] and the specified status.
     */
    private fun handleException(e: Exception, status: HttpStatus): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse.fromException(e), status)
}

/**
 * Data class representing the structure of the error response returned by the API.
 *
 * @property type The qualified name of the exception type.
 * @property message The error message associated with the exception.
 */
data class ErrorResponse(val type: String?, val message: String?) {
    companion object {
        /**
         * Creates an [ErrorResponse] from the given [Exception].
         *
         * @param e The exception from which the error response is created.
         * @return An instance of [ErrorResponse] containing the exception type and message.
         */
        fun fromException(e: Exception): ErrorResponse = ErrorResponse(type = e::class.qualifiedName, message = e.message)
    }
}