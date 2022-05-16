package com.alexandreladriere.formulix.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class Circuit(
    @SerializedName("circuitId")
    val circuitId: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("circuitName")
    val circuitName: String? = null,

    @SerializedName("Location")
    val location: Location? = null
)
