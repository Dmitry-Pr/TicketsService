package com.ticketingsystem.booking.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderRequest(
    @JsonProperty("from_station_id")
    val fromStationId: Int,
    @JsonProperty("to_station_id")
    val toStationId: Int
)