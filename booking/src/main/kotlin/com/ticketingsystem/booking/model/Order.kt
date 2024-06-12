package com.ticketingsystem.booking.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("order")
class Order(
    @Id
    private val id: Int? = null,
    @Column("user_id")
    private val userId: Int? = null,
    @Column("from_station_id")
    private val fromStationId: Int,
    @Column("to_station_id")
    private val toStationId: Int,
    private var status: Int,
    private val created: Timestamp
) {
    fun getId(): Int? = id
    fun getUserId(): Int? = userId
    fun getFromStationId(): Int = fromStationId
    fun getToStationId(): Int = toStationId
    fun getStatus(): Int = status
    fun setStatus(status: Int) {
        this.status = status
    }

    fun getCreated(): Timestamp = created
}