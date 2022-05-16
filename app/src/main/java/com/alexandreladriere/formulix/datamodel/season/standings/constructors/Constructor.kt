package com.alexandreladriere.formulix.datamodel.season.standings.constructors

import com.google.gson.annotations.SerializedName

data class Constructor(
    @SerializedName("constructorId")
    val constructorId: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null,
)
