package com.alexandreladriere.formulix.api

import com.alexandreladriere.formulix.datamodel.season.races.SeasonRacesResponse
import com.alexandreladriere.formulix.datamodel.season.standings.constructors.SeasonConstructorStandingsResponse
import com.alexandreladriere.formulix.datamodel.season.standings.drivers.SeasonDriverStandingsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ErgastApi {
    @GET("/api/f1/current.json")
    suspend fun getSeasonRaces() : Response<SeasonRacesResponse>

    @GET("/api/f1/current/constructorStandings.json")
    suspend fun getSeasonConstructorStandings() : Response<SeasonConstructorStandingsResponse>

    @GET("/api/f1/current/driverStandings.json")
    suspend fun getSeasonDriverStandings() : Response<SeasonDriverStandingsResponse>
}