package com.example.stockktsample.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity(name = "stocks")
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    var currentPrice: BigDecimal,
) {
    var lastUpdate: Instant = Instant.now()

    @PreUpdate
    fun preUpdate() {
        lastUpdate = Instant.now()
    }
}
