package com.sarabyeet.portalworlds.network

import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetCharactersPageResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodesPageResponse
import retrofit2.Response

class ApiClient(private val rickAndMortyService: RickAndMortyService) {

    suspend fun getCharacterById(characterId: Int): SimpleResponse<GetCharacterByIdResponse> {
        return safeApiCall { rickAndMortyService.getCharacterById(characterId) }
    }

    suspend fun getCharactersPage(pageIndex: Int): SimpleResponse<GetCharactersPageResponse> {
        return safeApiCall { rickAndMortyService.getCharactersPage(pageIndex) }
    }

    suspend fun getCharactersPage(
        characterName: String,
        pageIndex: Int
    ): SimpleResponse<GetCharactersPageResponse> {
        return safeApiCall { rickAndMortyService.getCharactersPage(characterName, pageIndex) }
    }

    suspend fun getEpisodeById(episodeId: Int): SimpleResponse<GetEpisodeByIdResponse> {
        return safeApiCall { rickAndMortyService.getEpisodeById(episodeId) }
    }

    suspend fun getEpisodeRange(episodeRange: String): SimpleResponse<List<GetEpisodeByIdResponse>> {
        return safeApiCall { rickAndMortyService.getEpisodeRange(episodeRange) }
    }

    suspend fun getEpisodesPage(pageIndex: Int):SimpleResponse<GetEpisodesPageResponse>{
        return safeApiCall { rickAndMortyService.getEpisodesPage(pageIndex) }
    }

    suspend fun getCharacterRange(characterRange: String): SimpleResponse<List<GetCharacterByIdResponse>> {
        return safeApiCall { rickAndMortyService.getCharacterRange(characterRange) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}
