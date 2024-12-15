package ru.mikhail.lab4_backend

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = e.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)

    }

    @ExceptionHandler(UnrecognizedPropertyException::class)
    fun handleUnrecognizedException(e: UnrecognizedPropertyException): ResponseEntity<Map<String, String?>> {
        val message = "Unrecognized property ${e.propertyName}"
        return ResponseEntity(mapOf("error" to message), HttpStatus.BAD_REQUEST)

    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Map<String, String?>> {
        val message = when (val rootCause = e.rootCause) {
            is MismatchedInputException -> {
                val fieldName = rootCause.path.joinToString(".") {it.fieldName ?: "unknown"}
                "Invalid data type for field: $fieldName. Excepted a numeric value."
            }
            else -> "Malformed JSON: ${rootCause?.message ?: e.message}"

        }
        return ResponseEntity(mapOf("error" to message), HttpStatus.BAD_REQUEST)
    }

}
