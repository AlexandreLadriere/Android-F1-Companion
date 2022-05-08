package com.alexandreladriere.f1companion.api

import com.alexandreladriere.f1companion.datamodel.season.races.SeasonRacesResponse
import com.alexandreladriere.f1companion.datamodel.season.standings.constructors.SeasonConstructorStandingsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ErgastApi {
    @GET("/api/f1/current.json")
    suspend fun getSeasonRaces() : Response<SeasonRacesResponse>

    @GET("/api/f1/current/constructorStandings.json")
    suspend fun getSeasonConstructorStandings() : Response<SeasonConstructorStandingsResponse>
}