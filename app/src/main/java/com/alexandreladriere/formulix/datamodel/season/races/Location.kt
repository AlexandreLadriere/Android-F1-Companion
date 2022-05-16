package com.alexandreladriere.formulix.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: String? = null,

    @SerializedName("long")
    val long: String? = null,

    @SerializedName("locality")
    val locality: String? = null,

    @SerializedName("country")
    val country: String? = null
)
