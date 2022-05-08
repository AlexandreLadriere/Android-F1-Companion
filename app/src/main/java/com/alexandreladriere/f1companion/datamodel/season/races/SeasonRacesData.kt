package com.alexandreladriere.f1companion.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class SeasonRacesData (
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

    @SerializedName("RaceTable")
    val raceTable: RaceTable? = null
)