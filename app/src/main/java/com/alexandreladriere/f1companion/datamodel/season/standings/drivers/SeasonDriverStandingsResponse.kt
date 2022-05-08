package com.alexandreladriere.f1companion.datamodel.season.standings.drivers

import com.google.gson.annotations.SerializedName

data class SeasonDriverStandingsResponse(
    @SerializedName("MRData")
    val data: DriverStandingsData? = null
)
