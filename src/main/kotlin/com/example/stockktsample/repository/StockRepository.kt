package com.example.stockktsample.repository

import com.example.stockktsample.entity.Stock
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long> {
    fun findStockByName(name: String): Stock?
    fun findAllBy(pageable: Pageable): List<Stock>
}