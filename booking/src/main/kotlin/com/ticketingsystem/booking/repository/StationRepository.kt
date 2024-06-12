package com.ticketingsystem.booking.repository

import com.ticketingsystem.booking.model.Station
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StationRepository : CrudRepository<Station, Int> {
    fun getById(id: Int): Station?
    override fun findAll(): List<Station>
}