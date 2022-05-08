package com.alexandreladriere.f1companion.datamodel.season.standings.constructors

import com.google.gson.annotations.SerializedName

data class StandingsList(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("round")
    val round: String? = null,

    @SerializedName("ConstructorStandings")
    val racesList: List<Standings>? = null,
)
