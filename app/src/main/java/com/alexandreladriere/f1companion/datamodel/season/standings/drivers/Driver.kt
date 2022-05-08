package com.alexandreladriere.f1companion.datamodel.season.standings.drivers

import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("driverId")
    val driverId: String? = null,

    @SerializedName("permanentNumber")
    val permanentNumber: String? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("givenName")
    val givenName: String? = null,

    @SerializedName("familyName")
    val familyName: String? = null,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null,
    )
