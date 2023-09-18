package com.example.stockktsample.api

import com.example.stockktsample.dto.CreateRequestDTO
import com.example.stockktsample.dto.StockDTO
import com.example.stockktsample.dto.UpdateRequestDTO
import com.example.stockktsample.service.StockService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stocks")
class StockController(
    private val stockService: StockService
) {

    @GetMapping
    fun stocks(pageable: Pageable): ResponseEntity<List<StockDTO>> {
        return ResponseEntity.ok(stockService.findStocks(pageable))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<StockDTO> {
        return ResponseEntity.ok(stockService.findStockBy(id))
    }

    @PostMapping
    fun createNew(@Valid @RequestBody createRequestDTO: CreateRequestDTO): ResponseEntity<Any> {
        stockService.createNewStock(createRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateBy(@Valid @RequestBody updateRequestDTO: UpdateRequestDTO): ResponseEntity<Any> {
        stockService.updateStock(updateRequestDTO)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteBy(@PathVariable("id") id: Long): ResponseEntity<Any> {
        stockService.deleteStock(id)
        return ResponseEntity.noContent().build()
    }
}