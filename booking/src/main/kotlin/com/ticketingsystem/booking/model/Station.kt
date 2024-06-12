package com.ticketingsystem.booking.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("station")
class Station(
    @Id
    private val id: Int? = null,
    private val station: String
) {
    fun getId(): Int? = id
    fun getStation(): String = station
}