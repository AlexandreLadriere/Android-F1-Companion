package com.alexandreladriere.formulix.datamodel.season.standings.drivers

import com.google.gson.annotations.SerializedName

data class DriverStandingsData(
    @SerializedName("xmlns")
    val xmlns: String? = null,

    @SerializedName("series")
    val series: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("limit")
    val limit: String? = null,

    @SerializedName("offset")
    val offset: String? = null,

    @SerializedName("total")
    val total: String? = null,

    @SerializedName("StandingsTable")
    val driverStandingsTable: DriverStandingsTable? = null
)
