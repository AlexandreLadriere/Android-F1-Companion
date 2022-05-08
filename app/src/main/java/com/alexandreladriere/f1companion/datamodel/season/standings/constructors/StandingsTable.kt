package com.alexandreladriere.f1companion.datamodel.season.standings.constructors

import com.google.gson.annotations.SerializedName

data class StandingsTable(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("StandingsLists")
    val racesList: StandingsList? = null,
)
