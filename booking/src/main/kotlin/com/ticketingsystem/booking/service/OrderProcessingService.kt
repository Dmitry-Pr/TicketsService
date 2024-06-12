package com.ticketingsystem.booking.service

import com.ticketingsystem.booking.repository.OrderRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class OrderProcessingService(
    private val orderRepository: OrderRepository
) {
    @Scheduled(fixedRate = 5000) // every 5 seconds
    @Transactional
    fun processOrders() {
        val ordersToCheck = orderRepository.findByStatus(1)
        ordersToCheck.forEach { order ->
            order.setStatus(if (Random.nextBoolean()) 2 else 3)
            orderRepository.save(order)
        }
    }
}
