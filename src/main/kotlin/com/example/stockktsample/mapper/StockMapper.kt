package com.example.stockktsample.mapper

import com.example.stockktsample.dto.CreateRequestDTO
import com.example.stockktsample.dto.StockDTO
import com.example.stockktsample.entity.Stock
import org.springframework.stereotype.Component

@Component
class StockMapper {
    fun mapToDTO(stock: Stock): StockDTO {
        return StockDTO(
            id = stock.id!!,
            name = stock.name,
            currentPrice = stock.currentPrice,
            lastUpdate = stock.lastUpdate
        )
    }

    fun mapToEntity(createRequestDTO: CreateRequestDTO): Stock {
        return Stock(name = createRequestDTO.name, currentPrice = createRequestDTO.price)
    }
}
