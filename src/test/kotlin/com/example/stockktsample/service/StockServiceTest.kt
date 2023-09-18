package com.example.stockktsample.service

import com.example.stockktsample.dto.CreateRequestDTO
import com.example.stockktsample.dto.UpdateRequestDTO
import com.example.stockktsample.entity.Stock
import com.example.stockktsample.exception.StockIsAlreadyExistsException
import com.example.stockktsample.exception.StockNotFoundException
import com.example.stockktsample.`given 5 random stock`
import com.example.stockktsample.`given a random stock`
import com.example.stockktsample.mapper.StockMapper
import com.example.stockktsample.repository.StockRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.Optional


class StockServiceTest {
    private lateinit var mockStockRepository: StockRepository
    private lateinit var stockService: StockService
    private lateinit var stockMapper: StockMapper

    @BeforeEach
    fun setup() {
        mockStockRepository = mock()
        stockMapper = StockMapper()
        stockService = StockService(mockStockRepository, stockMapper)
    }

    @Test
    fun `New Stock should be created if there is no stock with its name in DB`() {
        val newStockName = "new-stock"
        stockService.createNewStock(createRequestDTO = CreateRequestDTO(
            name = "new-stock",
            price = BigDecimal(100)
        ))

        val argumentCaptor = ArgumentCaptor.forClass(Stock::class.java)
        verify(mockStockRepository, times(1)).findStockByName(newStockName)
        verify(mockStockRepository, times(1)).save(argumentCaptor.capture())
    }

    @Test
    fun `New Stock shouldn't be created if there is no stock with its name in DB`() {
        val randomStock = `given a random stock` {}
        whenever(mockStockRepository.findStockByName(randomStock.name)).thenReturn(randomStock)
        assertThrows<StockIsAlreadyExistsException> {
            stockService.createNewStock(
                CreateRequestDTO(
                    name = randomStock.name,
                    price = BigDecimal(100)
                )
            )
        }

        val argumentCaptor = ArgumentCaptor.forClass(Stock::class.java)
        verify(mockStockRepository, times(0)).save(argumentCaptor.capture())
    }

    @Test
    fun `Stock shouldn't be deleted if there is not stock in DB with id`() {
        assertThrows<StockNotFoundException> {
            stockService.deleteStock(id = 9L)
        }
    }

    @Test
    fun `Stock should be deleted if we can find the stock id in DB`() {
        val randomStock = `given a random stock` {}
        whenever(mockStockRepository.findById(randomStock.id!!)).thenReturn(Optional.of(randomStock))

        stockService.deleteStock(id = randomStock.id!!)

        val argumentCaptor = ArgumentCaptor.forClass(Stock::class.java)
        verify(mockStockRepository, times(1)).delete(argumentCaptor.capture())
    }

    @Test
    fun `Stock shouldn't be updated if there is not stock in DB with id`() {
        val randomStock = `given a random stock` {}
        whenever(mockStockRepository.findById(randomStock.id!!)).thenReturn(Optional.of(randomStock))

        assertThrows<StockNotFoundException> {
            stockService.updateStock(
                UpdateRequestDTO(id = 9L, price = BigDecimal(200))
            )
        }

        val argumentCaptor = ArgumentCaptor.forClass(Stock::class.java)
        verify(mockStockRepository, times(0)).save(argumentCaptor.capture())
    }

    @Test
    fun `Stock should be updated if we can find the stock id in DB`() {
        val randomStock = `given a random stock` {}
        whenever(mockStockRepository.findById(randomStock.id!!)).thenReturn(Optional.of(randomStock))

        stockService.updateStock(
            UpdateRequestDTO(id = randomStock.id!!, price = BigDecimal(200))
        )

        val argumentCaptor = ArgumentCaptor.forClass(Stock::class.java)
        verify(mockStockRepository, times(1)).save(argumentCaptor.capture())
    }

    @Test
    fun `Stocks should be return if we can stock in DB`() {
        val pageable = Pageable.ofSize(5)
        val stocks = `given 5 random stock` {}

        whenever(mockStockRepository.findAllBy(pageable)).thenReturn(stocks)

        val actualStocks = stockService.findStocks(pageable)

        assertThat(actualStocks.map { it.id }).isEqualTo(stocks.map { it.id })
    }
}