package com.alexandreladriere.f1companion.datamodel.season.standings.drivers

import com.google.gson.annotations.SerializedName

data class DriverStandingsList(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("round")
    val round: String? = null,

    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStandings>? = null,
)
