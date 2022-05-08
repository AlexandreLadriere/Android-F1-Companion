package com.alexandreladriere.f1companion.datamodel.season.standings.drivers

import com.alexandreladriere.f1companion.datamodel.season.standings.constructors.Constructor
import com.google.gson.annotations.SerializedName

data class DriverStandings(
    @SerializedName("position")
    val position: String? = null,

    @SerializedName("positionText")
    val positionText: String? = null,

    @SerializedName("points")
    val points: String? = null,

    @SerializedName("wins")
    val wins: String? = null,

    @SerializedName("Driver")
    val driver: Driver? = null,

    @SerializedName("Constructors")
    val constructor: List<Constructor>? = null,
)
