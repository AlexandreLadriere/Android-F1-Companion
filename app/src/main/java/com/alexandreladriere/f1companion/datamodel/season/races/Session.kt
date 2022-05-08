package com.alexandreladriere.f1companion.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("date")
    val date: String? = null,

    @SerializedName("time")
    val time: String? = null
)
