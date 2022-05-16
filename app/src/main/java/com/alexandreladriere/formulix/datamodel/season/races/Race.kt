package com.alexandreladriere.formulix.datamodel.season.races

import com.google.gson.annotations.SerializedName

data class Race(
    @SerializedName("season")
    val season: String? = null,

    @SerializedName("round")
    val round: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("raceName")
    val raceName: String? = null,

    @SerializedName("Circuit")
    val circuit: Circuit? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("time")
    val time: String? = null,

    @SerializedName("FirstPractice")
    val firstPractice: Session? = null,

    @SerializedName("SecondPractice")
    val secondPractice: Session? = null,

    @SerializedName("ThirdPractice")
    val thirdPractice: Session? = null,

    @SerializedName("Qualifying")
    val qualifying: Session? = null,

    @SerializedName("Sprint")
    val sprint: Session? = null
)
