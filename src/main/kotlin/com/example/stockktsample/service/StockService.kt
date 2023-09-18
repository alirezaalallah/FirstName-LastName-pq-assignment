package com.example.stockktsample.service

import com.example.stockktsample.dto.CreateRequestDTO
import com.example.stockktsample.dto.StockDTO
import com.example.stockktsample.dto.UpdateRequestDTO
import com.example.stockktsample.entity.Stock
import com.example.stockktsample.exception.StockIsAlreadyExistsException
import com.example.stockktsample.exception.StockNotFoundException
import com.example.stockktsample.mapper.StockMapper
import com.example.stockktsample.repository.StockRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class StockService(
    private val stockRepository: StockRepository,
    private val stockMapper: StockMapper
) {
    fun findStockBy(id: Long): StockDTO = getStockByIdOrThrows(id)
        .let(stockMapper::mapToDTO)

    fun deleteStock(id: Long) = getStockByIdOrThrows(id)
        .apply {
            stockRepository.delete(this)
        }

    fun updateStock(updateRequestDTO: UpdateRequestDTO) =
        getStockByIdOrThrows(updateRequestDTO.id)
            .apply {
                this.currentPrice = updateRequestDTO.price
                stockRepository.save(this)
            }

    fun createNewStock(createRequestDTO: CreateRequestDTO) {
        if (stockRepository.findStockByName(createRequestDTO.name) != null) {
            throw StockIsAlreadyExistsException()
        }
        val stock = stockMapper.mapToEntity(createRequestDTO)
        stockRepository.save(stock)
    }

    fun findStocks(page: Pageable): List<StockDTO> =
        stockRepository.findAllBy(page)
            .map(stockMapper::mapToDTO)

    private fun getStockByIdOrThrows(id: Long): Stock =
        stockRepository.findById(id)
            .orElseThrow { StockNotFoundException() }
}