package com.alexandreladriere.f1companion.datamodel.season.constructorstandings

import com.alexandreladriere.f1companion.datamodel.season.races.SeasonRacesData
import com.google.gson.annotations.SerializedName

data class SeasonConstructorStandingsResponse(
    @SerializedName("MRData")
    val data: SeasonRacesData? = null
)
