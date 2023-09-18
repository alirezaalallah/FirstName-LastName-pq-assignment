package com.example.stockktsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StockWebApplication

fun main(args: Array<String>) {
	runApplication<StockWebApplication>(*args)
}
