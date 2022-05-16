package com.alexandreladriere.formulix.datamodel.season.standings.constructors

import com.google.gson.annotations.SerializedName

data class ConstructorStandingsTable(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("StandingsLists")
    val constructorStandingsList: List<ConstructorStandingsList>? = null,
)
