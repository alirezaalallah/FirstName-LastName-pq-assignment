package com.example.stockktsample.api

import com.example.stockktsample.dto.ErrorDTO
import com.example.stockktsample.dto.Reason
import com.example.stockktsample.exception.StockIsAlreadyExistsException
import com.example.stockktsample.exception.StockNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class StockControllerExceptionHandler {

    @ExceptionHandler(StockNotFoundException::class)
    fun handleStockNotFoundException(ex: StockNotFoundException, request: WebRequest): ResponseEntity<List<ErrorDTO>> {
        val errorDTO = listOf(
            ErrorDTO(reason = Reason.STOCK_NOT_FOUND, message = ex.message ?: "Stock not found")
        )

        return ResponseEntity(errorDTO, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StockIsAlreadyExistsException::class)
    fun handleStockNotFoundException(
        ex: StockIsAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<List<ErrorDTO>> {
        val errorDTO = listOf(
            ErrorDTO(reason = Reason.STOCK_IS_ALREADY_EXISTS, message = ex.message ?: "Stock is already exists")
        )

        return ResponseEntity(errorDTO, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<List<ErrorDTO>> {
        val errors = ex.bindingResult.fieldErrors
            .map { fieldError ->
                ErrorDTO(reason = Reason.INVALID_VALUE, message = fieldError.defaultMessage ?: "")
            }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnknownException(ex: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<List<ErrorDTO>> {
        val errorDTO = listOf(
            ErrorDTO(reason = Reason.INVALID_JSON, message = "Invalid json!")
        )
        return ResponseEntity(errorDTO, HttpStatus.BAD_REQUEST)
    }
}