package com.sarabyeet.portalworlds.arch

import com.sarabyeet.portalworlds.domain.mapers.CharacterMapper
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.network.NetworkLayer
import com.sarabyeet.portalworlds.network.PortalWorldsCache
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse
import kotlin.random.Random

class SharedRepository {

    suspend fun getCharacterById(characterId: Int): Character? {
        val request = NetworkLayer.api.getCharacterById(characterId)

        // Check for caching
        val cachedCharacter = PortalWorldsCache.characterMap[characterId]
        cachedCharacter?.let { character ->
            return character
        }
        /**
         * request is failed due to server error that is out of our hands
         * */
        if (request.isFailed) {
            return null
        }

        /**
         * request is send to the server, but wasn't an appropriate one
         * Not an OK request
         * */
        if (!request.isSuccessful) {
            return null
        }

        val networkEpisodes = getEpisodesFromCharacterResponse(request.body)
        val character = CharacterMapper.buildFrom(response = request.body, networkEpisodes)

        // update cache and return character
        PortalWorldsCache.characterMap[characterId] = character
        return character
    }

    suspend fun getRandomCharacter(): Character? {
        val randomCharacterId = Random.nextInt(0,826)
        val request = NetworkLayer.api.getCharacterById(randomCharacterId)
        /**
         * request is failed due to server error that is out of our hands
         * */
        if (request.isFailed) {
            return null
        }
        /**
         * request is send to the server, but wasn't an appropriate one
         * Not an OK request
         * */
        if (!request.isSuccessful) {
            return null
        }
        val networkEpisodes = getEpisodesFromCharacterResponse(request.body)
        return CharacterMapper.buildFrom(response = request.body, networkEpisodes)
    }

    private suspend fun getEpisodesFromCharacterResponse(response: GetCharacterByIdResponse): List<GetEpisodeByIdResponse> {
        val episodeRange = response.episode.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }.toString()

        val request = NetworkLayer.api.getEpisodeRange(episodeRange)

        if (request.isFailed || !request.isSuccessful) {
            return emptyList()
        }

        return request.body
    }
}
