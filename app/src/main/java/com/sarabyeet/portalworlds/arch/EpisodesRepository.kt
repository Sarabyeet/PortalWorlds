package com.sarabyeet.portalworlds.arch

import com.sarabyeet.portalworlds.domain.mapers.EpisodeMapper
import com.sarabyeet.portalworlds.domain.models.Episode
import com.sarabyeet.portalworlds.network.NetworkLayer
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodesPageResponse

class EpisodesRepository {

    suspend fun getEpisodesById(episodeId: Int): Episode? {
        val request = NetworkLayer.api.getEpisodeById(episodeId)
        if (!request.isSuccessful) {
            return null
        }

        val networkCharacter = getCharactersFromEpisode(request.body)
        return EpisodeMapper.buildFrom(
            request.body,
            networkCharacters = networkCharacter
        )
    }

    private suspend fun getCharactersFromEpisode(episodeById: GetEpisodeByIdResponse): List<GetCharacterByIdResponse> {
        val list = episodeById.characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }.toString()
        val request = NetworkLayer.api.getCharacterRange(list)
        return request.bodyNullable ?: emptyList()
    }

    suspend fun getEpisodesPage(pageIndex: Int): GetEpisodesPageResponse? {
        val pageRequest = NetworkLayer.api.getEpisodesPage(pageIndex)

        if (!pageRequest.isSuccessful) {
            return null
        }
        return pageRequest.body
    }
}