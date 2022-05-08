package com.alexandreladriere.f1companion.datamodel.season.standings.drivers

import com.alexandreladriere.f1companion.datamodel.season.standings.constructors.StandingsList
import com.google.gson.annotations.SerializedName

data class DriverStandingsTable(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("StandingsLists")
    val driverStandingsList: List<DriverStandingsList>? = null,
)
