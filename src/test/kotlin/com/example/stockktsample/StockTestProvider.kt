package com.example.stockktsample

import com.example.stockktsample.entity.Stock

fun `given a random stock`(block: () -> Unit) = buildRandomStock {}

fun `given 5 random stock`(block: () -> Unit) = buildRandomStocks(amount = 5) {}

fun `a stock with`(block: StockBuilderDSL.() -> Unit) = buildRandomStock(block)

private fun buildRandomStocks(amount: Int, block: StockBuilderDSL.() -> Unit): MutableList<Stock> {
    return (1..amount)
        .map {
            buildRandomStock {
                block()
            }
        }.toMutableList()
}

private fun buildRandomStock(block: StockBuilderDSL.() -> Unit): Stock {
    val stock = Stock(
        id = (1L..100L).random(),
        name = "Company - ${(100..1000).random()}",
        currentPrice = (0..1_000_000).random().toBigDecimal()
    )
    block(StockBuilderDSL(stock))
    return stock
}

class StockBuilderDSL(private val stock: Stock) {
    var name by stock::name
    var currentPrice by stock::currentPrice
}