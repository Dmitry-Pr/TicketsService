package com.ticketingsystem.booking.repository

import com.ticketingsystem.booking.model.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, Int> {
    fun findByUserId(userId: Int?): List<Order>
    fun findByStatus(status: Int): List<Order>
}