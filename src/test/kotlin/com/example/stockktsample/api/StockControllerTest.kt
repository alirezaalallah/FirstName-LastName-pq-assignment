package com.example.stockktsample.api

import com.example.stockktsample.dto.CreateRequestDTO
import com.example.stockktsample.dto.ErrorDTO
import com.example.stockktsample.dto.Reason
import com.example.stockktsample.dto.UpdateRequestDTO
import com.example.stockktsample.entity.Stock
import com.example.stockktsample.`given 5 random stock`
import com.example.stockktsample.`given a random stock`
import com.example.stockktsample.repository.StockRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.hasValue
import org.hamcrest.Matchers.`is`
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StockControllerTest @Autowired constructor(
    val mvc: MockMvc, val stockRepository: StockRepository,
    val objectMapper: ObjectMapper
) {
    private val url = "/api/stocks"
    private val updateOrDeleteOrOneUrl = "/api/stocks/{id}"

    @BeforeEach
    fun beforeEach() {
        stockRepository.deleteAll()
    }

    @Test
    fun `Stock should be return if there is a stock in db with specific id`() {
        val existingStock  = `given a random stock` {}
            .apply { stockRepository.save(this) }
        val id = stockRepository.findStockByName(existingStock.name)!!.id
        mvc.get(updateOrDeleteOrOneUrl,id) {
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$.name", equalTo(existingStock.name))
            jsonPath("$.currentPrice", `is`(existingStock.currentPrice.toInt()))
        }
    }

    @Test
    fun `Stock should be created if createRequest is sent valid`() {
        val createRequest = CreateRequestDTO(
            name = "stock-a",
            price = BigDecimal(100)
        )
        mvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createRequest)
        }.andExpect {
            status { isCreated() }
        }

        val createdStock = stockRepository.findStockByName(createRequest.name)
        assertNotNull(createdStock)
    }

    @Test
    fun `Stock should be updated if updateRequest is sent valid`() {
        val existingStock = `given a random stock` {}

        stockRepository.save(existingStock)
        val id = stockRepository.findStockByName(existingStock.name)!!.id
        val updateRequest = UpdateRequestDTO(id = id!!, price = BigDecimal(100))

        mvc.patch(updateOrDeleteOrOneUrl, existingStock.id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
        }
            .andExpect {
                status { isOk() }
            }

        val updatedStock = stockRepository.findStockByName(existingStock.name)
        assertThat(updatedStock)
            .extracting(Stock::currentPrice.name)
            .isEqualTo(updateRequest.price)
    }

    @Test
    fun `Stock should be deleted if stock is already in the DB`() {
        val existingStock = `given a random stock` {}

        stockRepository.save(existingStock)
        val id = stockRepository.findStockByName(existingStock.name)!!.id
        mvc.delete(updateOrDeleteOrOneUrl, id) {
        }.andExpect {
            status { isNoContent() }
        }

        val updatedStock = stockRepository.findStockByName(existingStock.name)
        assertThat(updatedStock).isEqualTo(null)
    }

    @Test
    fun `Should return all stocks based on pagination parameter if there are stock in db available`() {
        val stocks =
            `given 5 random stock` {}
                .apply {
                    stockRepository.saveAll(this)
                }
        mvc.get(url) {
            param("page", "0")
            param("size", "5")
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$", hasSize<String>(5))
            for (i in 0 until stocks.size) {
                val stock = stocks[i]
                jsonPath("$[$i].name", equalTo(stock.name))
                jsonPath("$[$i].currentPrice", `is`(stock.currentPrice.toInt()))
            }
        }
    }

    @Test
    fun `Should return error if get stock which doesn't exists in DB`() {
        mvc.get(updateOrDeleteOrOneUrl,100) {
        }.andExpect {
            status { isNotFound() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$", hasSize<String>(1))
            jsonPath("$[0].reason", equalTo(Reason.STOCK_NOT_FOUND.name))
            jsonPath("$[0].message", equalTo("Stock not found"))
        }
    }

    @Test
    fun `Should return error if send create request for a stock which is already exists`() {
        val stock =
            `given a random stock` { }
                .apply {
                    stockRepository.save(this)
                }
        val createRequest = CreateRequestDTO(
            name = stock.name,
            price = BigDecimal(100)
        )

        mvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createRequest)
        }.andExpect {
            status { isBadRequest() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$", hasSize<String>(1))
            jsonPath("$[0].reason", equalTo(Reason.STOCK_IS_ALREADY_EXISTS.name))
            jsonPath("$[0].message", equalTo("Stock is already exists"))
        }
    }

    @Test
    fun `Should return error list if send invalid create request`() {
        val createRequest = CreateRequestDTO(
            name = "",
            price = BigDecimal(-100)
        )

        val expectedErrors = listOf(
            ErrorDTO(reason = Reason.INVALID_VALUE, message = "name should not be empty"),
            ErrorDTO(reason = Reason.INVALID_VALUE, message = "price should be positive value")
        )

        mvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createRequest)
        }.andExpect {
            status { isBadRequest() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$", hasSize<String>(2))
            containsInAnyOrder(
                expectedErrors
            )
        }
    }
}