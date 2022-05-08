package com.alexandreladriere.f1companion.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class RaceTable(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("Races")
    val racesList: List<Race>? = null,
)
