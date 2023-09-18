package com.example.stockktsample.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StockDTO(
    val id: Long,
    var name: String,
    var currentPrice: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var lastUpdate: Instant
)

enum class Reason {
    INVALID_VALUE,STOCK_NOT_FOUND, STOCK_IS_ALREADY_EXISTS, INVALID_JSON
}

data class ErrorDTO(
    val reason: Reason,
    val message: String
)

data class UpdateRequestDTO(
    @field:NotNull(message = "id should not be null")
    val id: Long,
    @field:NotNull(message = "price should not be null")
    @field:Positive(message = "price should be positive value")
    val price: BigDecimal
)

data class CreateRequestDTO(
    @field:NotBlank(message = "name should not be empty")
    @field:NotNull(message = "name should not be null")
    val name: String,
    @field:NotNull(message = "price should not be empty")
    @field:Positive(message = "price should be positive value")
    val price: BigDecimal
)
