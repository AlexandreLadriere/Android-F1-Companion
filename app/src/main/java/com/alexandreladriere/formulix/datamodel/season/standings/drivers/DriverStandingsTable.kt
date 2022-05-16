package com.alexandreladriere.formulix.datamodel.season.standings.drivers

import com.google.gson.annotations.SerializedName

data class DriverStandingsTable(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("StandingsLists")
    val driverStandingsList: List<DriverStandingsList>? = null,
)
