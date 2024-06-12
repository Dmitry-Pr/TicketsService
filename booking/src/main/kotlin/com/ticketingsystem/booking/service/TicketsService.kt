package com.ticketingsystem.booking.service


import com.ticketingsystem.booking.api.model.OrderRequest
import com.ticketingsystem.booking.model.Order
import com.ticketingsystem.booking.repository.OrderRepository
import com.ticketingsystem.booking.repository.StationRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.sql.Timestamp
import java.time.Instant
import org.slf4j.LoggerFactory
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
@ExperimentalEncodingApi
class TicketService(
    private val orderRepository: OrderRepository,
    private val stationRepository: StationRepository,
    private val jwtService: JwtService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtService::class.java)
    }
    @Transactional
    fun makeOrder(@RequestBody orderRequest: OrderRequest, @CookieValue jwt: String?): HttpEntity<String> {
        if (jwt == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication required")

        if (!jwtService.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session got expired")
        }
        logger.info("Order request: $orderRequest")
        val fromStationId = stationRepository.getById(orderRequest.fromStationId)?.getId()
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid fromStationId")
        val toStationId = stationRepository.getById(orderRequest.toStationId)?.getId()
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid toStationId")
        if (toStationId == fromStationId)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't order a ticket to the same station")

        val userId = jwtService.extractUserId(jwt)
        orderRepository.save(
            Order(
                userId = userId,
                fromStationId = fromStationId,
                toStationId = toStationId,
                status = 1,
                created = Timestamp.from(Instant.now())
            )
        )

        return ResponseEntity.status(HttpStatus.OK).body("Order created successfully")
    }


    @Transactional
    fun getStations(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(stationRepository.findAll())
    }

    @Transactional
    fun getOrders(@CookieValue jwt: String?): ResponseEntity<Any> {
        if (jwt == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication required")

        if (!jwtService.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session got expired")
        }

        val orders = orderRepository.findByUserId(jwtService.extractUserId(jwt))
        if (orders.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No orders found")

        return ResponseEntity.status(HttpStatus.OK).body(orders)
    }

    @Transactional
    fun getOrder(@PathVariable orderId: Int, @CookieValue jwt: String?): ResponseEntity<Any> {
        if (jwt == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authentication required")

        if (!jwtService.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session got expired")
        }

        val order = orderRepository.findById(orderId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found")

        if (order.getUserId() != jwtService.extractUserId(jwt))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have access to this order")

        return ResponseEntity.status(HttpStatus.OK).body(order)
    }
}