package com.alexandreladriere.formulix.datamodel.season.standings.constructors

import com.google.gson.annotations.SerializedName

data class ConstructorStandings(
    @SerializedName("position")
    val position: String? = null,

    @SerializedName("positionText")
    val positionText: String? = null,

    @SerializedName("points")
    val points: String? = null,

    @SerializedName("wins")
    val wins: String? = null,

    @SerializedName("Constructor")
    val constructor: Constructor? = null,
)
