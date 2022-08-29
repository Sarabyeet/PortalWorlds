package com.sarabyeet.portalworlds.network

import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetCharactersPageResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodesPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {
    @GET("character/{character-id}")
    suspend fun getCharacterById(
        @Path("character-id") characterId: Int,
    ): Response<GetCharacterByIdResponse>

    @GET("character/")
    suspend fun getCharactersPage(
        @Query("page") pageIndex: Int,
    ): Response<GetCharactersPageResponse>

    @GET("character/")
    suspend fun getCharactersPage(
        @Query("name") characterName: String,
        @Query("page") pageIndex: Int
    ): Response<GetCharactersPageResponse>

    @GET("character/{character-range}")
    suspend fun getCharacterRange(
        @Path("character-range") episodeRange: String,
    ): Response<List<GetCharacterByIdResponse>>

    @GET("episode/{episode-id}")
    suspend fun getEpisodeById(
        @Path("episode-id") episodeId: Int,
    ): Response<GetEpisodeByIdResponse>

    @GET("episode/{episode-range}")
    suspend fun getEpisodeRange(
        @Path("episode-range") episodeRange: String,
    ): Response<List<GetEpisodeByIdResponse>>

    @GET("episode/")
    suspend fun getEpisodesPage(
        @Query("page") pageIndex: Int,
    ): Response<GetEpisodesPageResponse>
}