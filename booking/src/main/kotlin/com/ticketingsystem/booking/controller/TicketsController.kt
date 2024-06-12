package com.ticketingsystem.booking.controller

import com.ticketingsystem.booking.api.model.OrderRequest
import com.ticketingsystem.booking.service.TicketService
import org.springframework.web.bind.annotation.*
import kotlin.io.encoding.ExperimentalEncodingApi

@RestController
@RequestMapping("booking")
@ExperimentalEncodingApi
class TicketsController(
    private val ticketService: TicketService
) {
    @PostMapping("/make-order")
    fun makeOrder(@RequestBody orderRequest: OrderRequest, @CookieValue("jwt") jwt: String?) = ticketService.makeOrder(orderRequest, jwt)

    @GetMapping("/stations")
    fun getStations() = ticketService.getStations()

    @GetMapping("/my-orders")
    fun orders(@CookieValue("jwt") jwt: String?) = ticketService.getOrders(jwt)

    @GetMapping("/order/{orderId}")
    fun order(@PathVariable orderId: Int, @CookieValue("jwt") jwt: String?) = ticketService.getOrder(orderId, jwt)
}