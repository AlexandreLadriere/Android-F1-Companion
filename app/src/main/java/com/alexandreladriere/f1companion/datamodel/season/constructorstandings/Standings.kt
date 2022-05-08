package com.alexandreladriere.f1companion.datamodel.season.constructorstandings

import com.google.gson.annotations.SerializedName

data class Standings(
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
